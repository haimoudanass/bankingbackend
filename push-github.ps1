# Push automatique vers GitHub (compte: haimoudanass)
param(
    [string]$Message = "Update: $(Get-Date -Format 'yyyy-MM-dd HH:mm')"
)

$remoteUrl = "https://haimoudanass@github.com/haimoudanass/bankingbackend.git"
if ((git remote get-url origin 2>$null) -ne $remoteUrl) {
    git remote set-url origin $remoteUrl
    Write-Host "Remote configure : $remoteUrl"
}

git add .
$status = git status --porcelain
if (-not $status) {
    Write-Host "Rien a committer."
    exit 0
}

git commit -m $Message
git push origin main
Write-Host "Push termine : https://github.com/haimoudanass/bankingbackend"
