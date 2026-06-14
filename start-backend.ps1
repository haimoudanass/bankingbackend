# Arrete l'ancienne instance Java sur le port 8085 puis demarre Spring Boot
$port = 8085
$connections = Get-NetTCPConnection -LocalPort $port -State Listen -ErrorAction SilentlyContinue

if ($connections) {
    $pids = $connections | Select-Object -ExpandProperty OwningProcess -Unique
    foreach ($procId in $pids) {
        $proc = Get-Process -Id $procId -ErrorAction SilentlyContinue
        if ($proc -and $proc.ProcessName -eq 'java') {
            Write-Host "Arret de l'ancienne instance Java (PID $procId) sur le port $port..."
            Stop-Process -Id $procId -Force -ErrorAction SilentlyContinue
        }
    }
    Start-Sleep -Seconds 2
}

Write-Host "Demarrage du backend sur http://localhost:$port ..."
.\mvnw.cmd spring-boot:run -DskipTests
