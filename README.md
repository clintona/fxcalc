# FxCalc Overview
FxCalc is "a console-based currency converter application. The application allows a user to convert an amount in a specific currency to the equivalent amount in another currency."

# Installation
Extract the zipped Eclipse project to a directory.

# Usage
The FxCalc application is an executable JAR file. To run the program directly, type:
```sh
java -jar fxcalc.jar [-Doptions] {base} {amount} in {target}
``` 
Where:
- -Doptions is an optional setting - see Configuration below
- base = an ISO 4217 3 letter Currency code
- target = an ISO 4217 3 letter Currency code
- amount = a decimal amount to convert from base to target Currency

For convenience, there is also a Windows BATch script called 'fxcalc.bat' containing default system property, so you can run the program like:
```sh
fxcalc.bat {Currency} {amount} in {Currency}
``` 
The contents of fxcalc.bat are simply:
```sh
@echo off
java -Dconversions.file=src/main/resources/conversions.txt -Drates.file=src/main/resources/rates.txt -jar bin\fxcalc.jar %*
``` 

# Configuration
You can change the default behaviour of FxCalc using the following System Properties:

| System Property name | Description |
| ------ | ------ |
| rates.file | location of conversion rates file, containing eg: AUD/USD=0.8371|
| conversions.file | stores conversion methods, eg: AUD/USD=D (Direct lookup) |
| rounding.mode | java.Math.util.RoundingMode (default HALF_UP) |

To specify any of the above properties, try:
```sh
java -D rates.file=/path/to/rates.txt
```
## rates.file
A UTF-8 file of lines like:
```sh
<base>/<target>=<rate>
or
<base><target>=<rate>
```
Where:
- base = an ISO 4217 3 letter Currency code
- target = an ISO 4217 3 letter Currency code
- rate = a decimal amount to convert from base to target Currency

Note: you can use  optional whitespace around the '='

## conversions.file
A UTF-8 file of lines like:
```sh
<base>/<target>=<method>
```
Where:
- base = an ISO 4217 3 letter Currency code
- target = an ISO 4217 3 letter Currency code
- method = conversion method - refer requirements

# Design Notes
- Re-uses the ISO 4217 java.util.Currency.getFractionDigits() as the source of truth for the decimal precision of the target currency, ie: currencies using a variant of dollars tend to use 2 decimal places as the precision. The Japanese Yen (JPY) however, uses NO decimal places. This information is built-in to the java.util.Currency class, so there was no need to reinvent the wheel and derive the precision separately.

- Defaults Rounding to HALF_UP. Rounding only occurs on final output, to eliminate rounding error. BigDecimal is used to avoid inexact floating point calculation errors.

- designed for simplicity. No external libraries required (except junit). High code coverage. 
