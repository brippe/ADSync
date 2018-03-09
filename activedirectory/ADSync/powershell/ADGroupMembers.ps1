Param(
	[parameter(Mandatory=$true)] [alias("g")] $GroupName,
	[parameter(Mandatory=$true)] [alias("t")] $TermPath,
	[parameter(Mandatory=$true)] [alias("bid")] $BannerIDWithPath,
	[parameter(Mandatory=$true)] [alias("a")] $AddOrRemove,
	[parameter(Mandatory=$false)] [alias("m")] $message
)
Import-Module ActiveDirectory
Import-Module 'C:\Program Files (x86)\ADSync\powershell\Utils.psm1'
# needed to notify campuses to send messages to RabbitMQ
[System.Reflection.Assembly]::LoadFrom("C:\Program Files (x86)\ADSync\powershell\AMQP.dll")

$queue = ""
$term = $GroupName.Substring($GroupName.IndexOf("-")+1,$GroupName.LastIndexOf("-")-($GroupName.IndexOf("-")+1)) 
$crn = $GroupName.Substring($GroupName.IndexOf("-")+8,$GroupName.IndexOf(",")-($GroupName.IndexOf("-")+8))
$bid = $BannerIDWithPath
if($bid.Length > 8) {
	$bid =  $BannerIDWithPath.Substring($BannerIDWithPath.IndexOf("=")+1, $BannerIDWithPath.IndexOf(",")-($BannerIDWithPath.IndexOf("=")+1))
}
if ($GroupName.Contains("ACollege")) {
	$queue = "ACollege"
} elseif ($GroupName.Contains("BCollege")) {
	$queue = "BCollege"
} elseif ($GroupName.Contains("CCollege")) {
	$queue = "CCollege"
}
#.\ADGroupMembers.ps1 -g "CN=FC-201220-20922,OU=Groups,OU=BCollege,DC=Ripware,DC=local"
#-t "CN=FC-201220,OU=Groups,OU=BCollege,DC=Ripware,DC=local"
#-bid "CN=00001610,OU=People,DC=Ripware,DC=local" -a add

# Domain variables set in Spring Configuration file
#$Path = "OU=People,DC=Ripware,dc=local" 	# where do you want the user to be created
#$Domain = "Ripware.local"					# which domain are the users being created for?
try 
{	
	$members = Get-ADGroupMember -Identity $GroupName | Select-Object -Property sAMAccountName
	$isInGroup = $false
	foreach($member in $members) {
		if($member.sAMAccountName -eq $bid) {
			$isInGroup = $true
		}
	}
	
	if($AddOrRemove.CompareTo("add") -eq 0 -and !($isInGroup)) {
		Add-ADGroupMember -Identity $GroupName -Members $BannerIDWithPath -Confirm:$false
		if($message) {
			[AMQP.CampusAMQPSender]::publishMessage($queue, 
				"{ `"roster`": { `"bannerID`": `"$bid`", `"term`": `"$term`", `"crn`": `"$crn`", `"status`": `"add`" } }", $TRUE)
		}
	} elseif($AddOrRemove.CompareTo("remove") -eq 0 -and $isInGroup) {
		Remove-ADGroupMember -Identity $GroupName -Members $BannerIDWithPath -Confirm:$false
		if($message) {
			[AMQP.CampusAMQPSender]::publishMessage($queue, 
				"{ `"roster`": { `"bannerID`": `"$bid`", `"term`": `"$term`", `"crn`": `"$crn`", `"status`": `"drop`" } }", $TRUE)
		}
	} else {
		Write-Host "$bid already $AddOrRemove"
	}	
} catch {
	Write-Host "ERROR: $AddOrRemove user to a group"
	$error[0]
}

Remove-Module ActiveDirectory
Remove-Module Utils