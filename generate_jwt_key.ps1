# Generate Secure JWT Secret Key (256-bit)
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host "   JWT SECRET KEY GENERATOR (256-bit)" -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host ""

# Generate 32 random bytes (256 bits)
$bytes = New-Object byte[] 32
$rng = New-Object System.Security.Cryptography.RNGCryptoServiceProvider
$rng.GetBytes($bytes)

# Convert to Base64
$base64Key = [Convert]::ToBase64String($bytes)

# Display results
Write-Host "✅ Generated Secure JWT Secret Key:" -ForegroundColor Green
Write-Host ""
Write-Host $base64Key -ForegroundColor Yellow
Write-Host ""
Write-Host "📏 Length: $($base64Key.Length) characters" -ForegroundColor Cyan
Write-Host "🔒 Security: 256 bits (HS256 compliant)" -ForegroundColor Cyan
Write-Host ""
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host "   COPY THIS TO YOUR application.properties:" -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "jwt.secret=$base64Key" -ForegroundColor White -BackgroundColor DarkGreen
Write-Host ""
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host "   NEXT STEPS:" -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "1. Open your Spring Boot project" -ForegroundColor White
Write-Host "2. Edit: src/main/resources/application.properties" -ForegroundColor White
Write-Host "3. Replace the jwt.secret line with the one above" -ForegroundColor White
Write-Host "4. Save and restart your Spring Boot server" -ForegroundColor White
Write-Host "5. Test from your Android app" -ForegroundColor White
Write-Host ""
Write-Host "Press any key to exit..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
