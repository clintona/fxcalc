@echo off
java -Dconversions.file=src/main/resources/conversions.txt -Drates.file=src/main/resources/rates.txt -jar bin\fxcalc.jar %*
