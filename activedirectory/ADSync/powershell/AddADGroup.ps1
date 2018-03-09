Param(
	[parameter(Mandatory=$true)] [alias("g")] $GroupName,
	[parameter(Mandatory=$true)] [alias("p")] $Path,
	[parameter(Mandatory=$true)] [alias("mb")] $ManagedBy
)
Import-Module ActiveDirectory
Import-Module 'C:\Program Files (x86)\ADSync\powershell\Utils.psm1'

# Usage
#.\AddADGroup.ps1 -g FC-201220-020922 -p "OU=Groups,OU=BCollege,DC=Ripware,DC=local" -mb "CN=00001610,OU=People,DC=Ripware,DC=local"
# Domain variables set in Spring Configuration file
#$Path = "OU=People,DC=Ripware,dc=local" 	# where do you want the user to be created
#$Domain = "Ripware.local"					# which domain are the users being created for?
$SecurityGroup = ""

try 
{
	$SecurityGroup = Get-ADGroup -Identity $GroupName
	Write-Host "$GroupName Security Group exists"
} catch {
	Write-Host "Adding security group $GroupName"
	New-ADGroup -Name $GroupName -SamAccountName $GroupName -GroupCategory Security -GroupScope Universal -DisplayName $GroupName -Path $Path -ManagedBy $ManagedBy #-Credential $AdminCredential
	#Add to term and subject groups
}

Remove-Module ActiveDirectory
Remove-Module Utils