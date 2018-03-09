function AddUser
{
	[cmdletbinding()]
	Param($Name,
	$Password,
	$City,
	$Company, 
	$DisplayName,
	$Email,
	$BannerID,
	$Department,
	$GivenName,
	$Pcode,
	$State,
	$Street,
	$LastName,
	$Roles,
	$Domain,
	$Path,
	$addInfo)
			
	try {
		if($addInfo -eq "yes") 
		{	
			New-ADUser -Enabled 1 -UserPrincipalName "$BannerID@$Domain" -ChangePasswordAtLogon 0 -AccountPassword $Password -City $City -Company $Company -Country USA -Department $Department -DisplayName $DisplayName -EmailAddress $Email -EmployeeID $BannerID -GivenName $GivenName -Name $BannerID -Path $Path -PostalCode $Pcode -SamAccountName $BannerID -State $State -StreetAddress $Street -Surname $LastName -Description $Roles
		}
		else
		{
			New-ADUser -Enabled 1 -UserPrincipalName "$BannerID@$Domain" -ChangePasswordAtLogon 0 -AccountPassword $Password -EmployeeID $BannerID -Name $BannerID -Path $Path -SamAccountName $BannerID -DisplayName $DisplayName -Surname $LastName -GivenName $GivenName
		}
	} catch {}
}

function ModifyUser
{
	[cmdletbinding()]
	Param($Name,
	$Password,
	$City,
	$Company, 
	$DisplayName,
	$Email,
	$BannerID,
	$Department,
	$GivenName,
	$Pcode,
	$State,
	$Street,
	$LastName,
	$Roles,
	$Domain,
	$Path,
	$addInfo)

	try {
		Set-ADAccountPassword -Identity $BannerID -Reset -NewPassword $Password
		if($addInfo -eq "yes")
		{
			Write-Host "Modify user"
			#Set-ADUser -Enabled 1 -UserPrincipalName "$BannerID@$Domain" -ChangePasswordAtLogon 0 -Identity $BannerID -City $City -Company $Company -Country USA -Department $Department -DisplayName $DisplayName -EmailAddress $Email -EmployeeID $BannerID -GivenName $GivenName -PostalCode $Pcode -SamAccountName $BannerID -State $State -StreetAddress $Street -Surname $LastName -Description $Roles
			# By design don't change the enabled flag   -Enabled 1
			Set-ADUser -Identity $BannerID -ChangePasswordAtLogon 0 -City $City -DisplayName $DisplayName -EmailAddress $Email -EmployeeID $BannerID -GivenName $GivenName -PostalCode $Pcode -State $State -StreetAddress $Street -Surname $LastName -Description "$Roles" #-Replace @{c=$Country; company=$Company; department=$Department}
			# Issues setting the following attributes -Company $Company -Department $Department -Country USA 
		}
		else
		{
			Write-Host "Set-ADUser info for $BannerID"
			# By design don't change the enabled flag   -Enabled 1
			# Set-ADUser -UserPrincipalName "$BannerID@$Domain" -ChangePasswordAtLogon 0 -Identity $BannerID -EmailAddress $Email -EmployeeID $BannerID -SamAccountName $BannerID -DisplayName $DisplayName -Surname $LastName -GivenName $GivenName -Description "$Roles"
			Set-ADUser -Identity $BannerID  -EmailAddress $Email -EmployeeID $BannerID -DisplayName $DisplayName -Surname $LastName -GivenName $GivenName -SamAccountName $BannerID 
			# -Description "$Roles"
		}				
		
	} catch [Exception] {
		Write-Host "Error modifying user $BannerID $_.Exception.GetType().FullName, $_.Exception.Message"
	}
}