Param(
	[parameter(Mandatory=$true)] [alias("n")] $Name,
	[parameter(Mandatory=$true)] [alias("p")] $Password,
	[parameter(Mandatory=$true)] [alias("c")] $City,
	[parameter(Mandatory=$true)] [alias("comp")] $Company, 
	[parameter(Mandatory=$true)] [alias("dn")] $DisplayName,
	[parameter(Mandatory=$true)] [alias("e")] $Email,
	[parameter(Mandatory=$true)] [alias("bid")] $BannerID,
	[parameter(Mandatory=$true)] [alias("dept")] $Department,
	[parameter(Mandatory=$true)] [alias("gn")] $GivenName,
	[parameter(Mandatory=$true)] [alias("pc")] $Pcode,
	[parameter(Mandatory=$true)] [alias("s")] $State,
	[parameter(Mandatory=$true)] [alias("st")] $Street,
	[parameter(Mandatory=$true)] [alias("ln")] $LastName,
	[parameter(Mandatory=$false)] [alias("r")] [Object[]] $Roles, # can only specify one role in -Description
	[parameter(Mandatory=$true)] [alias("pat")] $Path,
	[parameter(Mandatory=$true)] [alias("dom")] $Domain,
	[parameter(Mandatory=$true)] [alias("ai")] $addInfo, # yes to add user information, no to not add user information to AD
	[parameter(Mandatory=$false)] [alias("m")] $message
)
Import-Module ActiveDirectory
Import-Module 'C:\Program Files (x86)\ADSync\powershell\Utils.psm1'

#$server = "amqp://<user>:<pass>@<server>:5672/"
#$server = "amqps://<user>:<pass>@<server>:5671/"

#Import the dlls needed to send messages to RabbitMQ
# Should only need to load the main
[System.Reflection.Assembly]::LoadFrom("C:\Program Files (x86)\ADSync\powershell\AMQP.dll")

# Domain variables set in Spring Configuration file
#$Path = "OU=People,DC=Ripware,dc=local" 	# where do you want the user to be created
#$Domain = "Ripware.local"					# which domain are the users being created for?

# Create a user
$queue = @()
foreach($r in $Roles) {
	if ($r.Contains("ACollege")) {
		$queue += "ACollege"
	} elseif ($r.Contains("BCollege")) {
		$queue += "BCollege"
	} elseif ($r.Contains("CCollege")) {
		$queue += "CCollege"
	}
}

# Valid Banner ID?
if(($BannerID.Length -ne 8 -or !($BannerID -match '^\d+$')) -and $message) {
	Write-Host "Failed to create user! Bad Banner ID: $BannerID"
	[AMQP.CampusAMQPSender]::publishMessage($queue, 
				"{ `"person`": { `"bannerID`": `"$Name`", `"fname`": `"$GivenName`", `"lname`": `"$LastName`", `"birthdate`": `"`", `"status`": `"failed: bad banner id $BannerID`" } }", $TRUE)
	exit
}

$userPassword = convertto-securestring $Password -asplaintext -force
$created = "created"

try 
{	
	$exists = Get-ADUser -Identity  $BannerID
	if (!$exists) 
	{ 
		Write-Host "User doesn't exist. Creating user!"
		AddUser -Name $Name -Password $userPassword -City $City -Company $Company -DisplayName $DisplayName -Email $Email -BannerID $BannerID -Department $Department -GivenName $GivenName -Pcode $Pcode -State $State -Street $Street -LastName $LastName -Roles $Roles -Domain $Domain -Path $Path -addInfo $addInfo
				
		$created = Get-ADUser -Identity  $BannerID
		if ($created -eq "") { 
			$created = "created"
			write-eventlog -logname Application -source ADSync_Ripware -eventID 3601 -entrytype Information -message "User $Name was created in Ripware.local"
		} else { $created = "failed" }
		
		# Added 3/6/2017
		if(((Get-ADUser -Identity $BannerID -Properties LockedOut).LockedOut)) {
			Unlock-ADAccount -Identity $BannerID
		}
		
		if ($message) {
			foreach($que in $queue)
			{
				[AMQP.CampusAMQPSender]::publishMessage($queue, 
					"{ `"person`": { `"bannerID`": `"$Name`", `"fname`": `"$GivenName`", `"lname`": `"$LastName`", `"birthdate`": `"`", `"status`": `"$created`" } }", $TRUE)
			}
		}
	}
	else 
	{
		Write-Host "************** AD User $Name exists **************"
		ModifyUser -Name $Name -Password $userPassword -City $City -Company $Company -DisplayName $DisplayName -Email $Email -BannerID $BannerID -Department $Department -GivenName $GivenName -Pcode $Pcode -State $State -Street $Street -LastName $LastName -Roles $Roles -Domain $Domain -Path $Path -addInfo $addInfo
	}
} catch {
	Write-Host "Error: User doesn't exist. Creating user!"	
	AddUser -Name $Name -Password $userPassword -City $City -Company $Company -DisplayName $DisplayName -Email $Email -BannerID $BannerID -Department $Department -GivenName $GivenName -Pcode $Pcode -State $State -Street $Street -LastName $LastName -Roles $Roles -Domain $Domain -Path $Path -addInfo $addInfo

	if ($message) {
		foreach($que in $queue)
		{
			[AMQP.CampusAMQPSender]::publishMessage($queue, 
					"{ `"person`": { `"bannerID`": `"$Name`", `"fname`": `"$GivenName`", `"lname`": `"$LastName`", `"birthdate`": `"`", `"status`": `"$created`" } }", $TRUE)
		}
	}
	write-eventlog -logname Application -source ADSync_Ripware -eventID 3601 -entrytype Information -message "User $Name was created in Ripware.local"
}

Remove-Module ActiveDirectory
Remove-Module Utils