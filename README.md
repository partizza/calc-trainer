# Calculation training shell application written on java 17

## Build and install distribution

> gradle installDist

## Run distribution

### interactive mode

*on Linux:*
```
 ./bin/calc-trainer
 calc-trainer:>train
```

*on Windows:*
```
 \bin\calc-trainer.bat
 calc-trainer:>train
```

### non-interactive mode

*on Linux:*
> ./bin/calc-trainer train

*on Windows:*
> \bin\calc-trainer.bat train


### running options

```
--digits int
  Sets the max count of digits in number
  [Optional, default = 2]

-a Boolean
   Includes operation of addition to training
   [Optional]

-s Boolean
   Includes operation of subtraction to training
   [Optional]

-m Boolean
   Includes operation of multiplication to training
   [Optional]

```

***Examples:***

*run training with 1-digit numbers and multiplication operation:*
> ./bin/calc-trainer train --digits 1 -m

*run training with 3-digit numbers and addition, subtraction operations:*
> ./bin/calc-trainer train --digits 1 -as

*running training with 2-digit numbers and addition, subtraction, multiplication operations:*
```
./bin/calc-trainer train
85 - 45 =  40
Correct!!!
93 + 53 =  146
Correct!!!
89 + 26 =  115
Correct!!!
28 * 87 =  456
Not correct!!! Correct answer: 2436
15 - 15 =  [Enter answer or 'stop' to exit]

```