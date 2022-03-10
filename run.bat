start runBack.bat
cd CurrencyConverterFront
start runFront.bat
timeout /t 15 /nobreak
start http://localhost:4200/convert