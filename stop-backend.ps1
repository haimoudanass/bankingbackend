# Arrete le backend Spring Boot qui ecoute sur le port 8085
$port = 8085
$connections = Get-NetTCPConnection -LocalPort $port -State Listen -ErrorAction SilentlyContinue

if (-not $connections) {
    Write-Host "Aucun processus n'ecoute sur le port $port."
    exit 0
}

$pids = $connections | Select-Object -ExpandProperty OwningProcess -Unique
foreach ($procId in $pids) {
    $proc = Get-Process -Id $procId -ErrorAction SilentlyContinue
    if ($proc) {
        Write-Host "Arret de $($proc.ProcessName) (PID $procId)..."
        Stop-Process -Id $procId -Force -ErrorAction SilentlyContinue
    }
}
Write-Host "Port $port libere."
