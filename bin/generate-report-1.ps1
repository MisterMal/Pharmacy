$PROJECT_ROOT = "$PSScriptRoot\.."

#build backend
Set-Location "$PROJECT_ROOT"



Set-Location docker
Start-Process -NoNewWindow docker "compose up"
Set-Location ..

#mvn clean package
