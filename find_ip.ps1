# Script tìm IP máy tính để cấu hình Android app

Write-Host "==================================================" -ForegroundColor Cyan
Write-Host "   TÌM IP MÁY TÍNH CHO ANDROID APP" -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host ""

# Get all IPv4 addresses
$ipAddresses = Get-NetIPAddress -AddressFamily IPv4 |
    Where-Object {$_.IPAddress -notlike "127.*" -and $_.IPAddress -notlike "169.254.*"} |
    Select-Object IPAddress, InterfaceAlias

Write-Host "📡 Các địa chỉ IP tìm thấy:" -ForegroundColor Green
Write-Host ""

$ipList = @()
$index = 1

foreach ($ip in $ipAddresses) {
    $ipList += $ip
    Write-Host "[$index] $($ip.IPAddress) - $($ip.InterfaceAlias)" -ForegroundColor Yellow
    $index++
}

if ($ipList.Count -eq 0) {
    Write-Host "❌ Không tìm thấy IP nào!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Hãy chạy: ipconfig" -ForegroundColor White
    exit
}

Write-Host ""
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host "   HƯỚNG DẪN CHỌN IP" -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host ""

# Suggest WiFi IP
$wifiIP = $ipList | Where-Object {$_.InterfaceAlias -like "*Wi-Fi*" -or $_.InterfaceAlias -like "*Wireless*"} | Select-Object -First 1

if ($wifiIP) {
    Write-Host "✅ KHUYẾN NGHỊ: Dùng IP WiFi" -ForegroundColor Green
    Write-Host ""
    Write-Host "   IP: $($wifiIP.IPAddress)" -ForegroundColor Yellow -BackgroundColor DarkGreen
    Write-Host "   Network: $($wifiIP.InterfaceAlias)" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "⚠️ LƯU Ý: Điện thoại và PC phải cùng WiFi!" -ForegroundColor Yellow
    Write-Host ""

    $selectedIP = $wifiIP.IPAddress
} else {
    # Use first IP
    $selectedIP = $ipList[0].IPAddress
    Write-Host "⚠️ Không tìm thấy WiFi, dùng IP đầu tiên" -ForegroundColor Yellow
    Write-Host "   IP: $selectedIP" -ForegroundColor Yellow
    Write-Host ""
}

Write-Host "==================================================" -ForegroundColor Cyan
Write-Host "   CẤU HÌNH ANDROID APP" -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "Mở file: ApiConfig.java" -ForegroundColor White
Write-Host ""
Write-Host "Thay đổi 2 dòng sau:" -ForegroundColor White
Write-Host ""

Write-Host "1. CURRENT_MODE:" -ForegroundColor Cyan
Write-Host "   private static final Mode CURRENT_MODE = Mode.REAL_DEVICE;" -ForegroundColor Green
Write-Host ""

Write-Host "2. YOUR_PC_IP:" -ForegroundColor Cyan
Write-Host "   private static final String YOUR_PC_IP = `"$selectedIP`";" -ForegroundColor Green -BackgroundColor DarkBlue
Write-Host ""

Write-Host "==================================================" -ForegroundColor Cyan
Write-Host "   COPY & PASTE CODE" -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host ""

# Generate code snippet
$codeSnippet = @"
// File: ApiConfig.java

private static final Mode CURRENT_MODE = Mode.REAL_DEVICE;
private static final String YOUR_PC_IP = "$selectedIP";
"@

Write-Host $codeSnippet -ForegroundColor White -BackgroundColor DarkGray
Write-Host ""

Write-Host "==================================================" -ForegroundColor Cyan
Write-Host "   TEST CONNECTION" -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "1. Đảm bảo Spring Boot đang chạy:" -ForegroundColor White
Write-Host "   ./mvnw spring-boot:run" -ForegroundColor Gray
Write-Host ""

Write-Host "2. Test từ browser PC:" -ForegroundColor White
Write-Host "   http://localhost:8080/api/auth/test" -ForegroundColor Gray
Write-Host ""

Write-Host "3. Test từ browser điện thoại:" -ForegroundColor White
Write-Host "   http://$selectedIP:8080/api/auth/test" -ForegroundColor Yellow
Write-Host ""

Write-Host "4. Nếu bước 3 thành công → Cấu hình đúng!" -ForegroundColor Green
Write-Host ""

Write-Host "==================================================" -ForegroundColor Cyan
Write-Host "   CHECK FIREWALL" -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "Chạy lệnh này để allow port 8080:" -ForegroundColor White
Write-Host ""

$firewallCommand = 'netsh advfirewall firewall add rule name="Spring Boot Server" dir=in action=allow protocol=TCP localport=8080'
Write-Host $firewallCommand -ForegroundColor White -BackgroundColor DarkRed
Write-Host ""

Write-Host "==================================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "Press any key to exit..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey('NoEcho,IncludeKeyDown')
