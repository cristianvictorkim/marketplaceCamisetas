param(
    [switch]$MigrateH2,
    [switch]$Local
)

$projectRoot = Split-Path -Parent $PSScriptRoot
$envFile = Join-Path $projectRoot ".env"

if (Test-Path $envFile) {
    foreach ($line in Get-Content $envFile) {
        $trimmedLine = $line.Trim()
        if (-not $trimmedLine -or $trimmedLine.StartsWith("#")) {
            continue
        }

        $parts = $trimmedLine -split "=", 2
        if ($parts.Count -eq 2) {
            [Environment]::SetEnvironmentVariable(
                $parts[0].Trim(),
                $parts[1].Trim(),
                "Process"
            )
        }
    }
}

if ($MigrateH2) {
    $env:MIGRATE_H2_TO_POSTGRES = "true"
}

if ($Local) {
    $env:SPRING_PROFILES_ACTIVE = "local"
}

& "$projectRoot\mvnw.cmd" `
    -s "$projectRoot\.mvn\settings.xml" `
    "-Dmaven.compiler.fork=true" `
    spring-boot:run

exit $LASTEXITCODE
