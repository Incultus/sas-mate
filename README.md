document is subject to change

Introduction

  * Build Arduino standard firmware
  * Build Java interface over serial connection
  * Build Java core functions
  * Build Java UI

Details

  == Arduino standard firmware ==

    * Design standard communication protocol between electrical design and microcontroller
    * Design serial communication protocol between microcontroller and Java
    * Document protocols

  == Java serial interface ==

    * implements serial communication libraries
    * multiple methods to convey information to the microcontroller which the microcontroller will then order the mechanical parts to function
    * methods to read information from compass and magnetic detector

  == Java core functions ==

    * methods to keep serial interface as user friendly as possible
    * uses higher order methods provided by the serial interface class
  
  == UI ==

    * controls Java core functions using buttons and text
    * simply maps all the methods in core function class to buttons