# Build & run the Yazbek compiler project with only a JDK (no Gradle needed).
#
# Usage:
#   .\build.ps1            # compile, then run app.Main on the sample project
#   .\build.ps1 -Run       # same as above
#   .\build.ps1 -NoRun     # compile only
#   .\build.ps1 -Test      # compile main + tests and run the test runner
#
# Compiles ONLY src/ (the gen/ directory is a stale duplicate of src/antlr and
# is intentionally excluded).

param(
    [switch]$NoRun,
    [switch]$Run,
    [switch]$Test
)

# Stop on PowerShell cmdlet errors, but NOT on native tool stderr: javac/java
# write progress and legitimate diagnostics to stderr, which under 'Stop' would
# otherwise abort the script. We detect real failures via $LASTEXITCODE instead.
$ErrorActionPreference = 'Continue'
$root = $PSScriptRoot
$jar  = Join-Path $root 'dependencies\antlr-4.13.2-complete.jar'
$out  = Join-Path $root 'out'
$sep  = [System.IO.Path]::PathSeparator   # ';' on Windows

if (-not (Test-Path $jar)) { throw "Missing ANTLR jar: $jar" }
if (Test-Path $out) { Remove-Item -Recurse -Force $out }
New-Item -ItemType Directory -Force -Path $out | Out-Null

# Collect source files for the main build, excluding the unit-test sources and
# the IDE's .antlr/ preview cache (stale duplicate copies of generated parsers).
$mainSources = Get-ChildItem -Recurse -Path (Join-Path $root 'src') -Filter *.java |
    Where-Object { $_.FullName -notmatch '\\src\\test\\' -and $_.FullName -notmatch '\\\.antlr\\' } |
    ForEach-Object { $_.FullName }

Write-Host "Compiling $($mainSources.Count) source files..." -ForegroundColor Cyan
& javac -cp $jar -d $out $mainSources
if ($LASTEXITCODE -ne 0) { throw "Compilation failed." }
Write-Host "Build OK -> $out" -ForegroundColor Green

if ($Test) {
    $testSources = Get-ChildItem -Recurse -Path (Join-Path $root 'src\test') -Filter *.java |
        ForEach-Object { $_.FullName }
    if ($testSources) {
        Write-Host "Compiling $($testSources.Count) test files..." -ForegroundColor Cyan
        & javac -cp "$out$sep$jar" -d $out $testSources
        if ($LASTEXITCODE -ne 0) { throw "Test compilation failed." }
    }
    Write-Host "Running tests..." -ForegroundColor Cyan
    & java "-Dfile.encoding=UTF-8" -cp "$out$sep$jar" test.TestRunner
    exit $LASTEXITCODE
}

if (-not $NoRun) {
    Write-Host "Running app.Main..." -ForegroundColor Cyan
    & java "-Dfile.encoding=UTF-8" -cp "$out$sep$jar" app.Main
}
