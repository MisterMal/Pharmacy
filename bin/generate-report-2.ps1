$PROJECT_ROOT = "$PSScriptRoot\.."

#build backend
Set-Location "$PROJECT_ROOT"


#mvn verify
mvn jacoco:dump jacoco:report

Set-Location docker
docker compose down
docker compose rm
Set-Location ..