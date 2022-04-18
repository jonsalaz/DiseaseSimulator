# Disease Simulation
## Authors: Jonathan Salazar and Cyrus McCormick

### How to use
In order to run the disease simulation please run the following line of code

```java -jar disease.jar <config_file.txt>```

replacing <config_file.txt> with your own configuring text file.

Please note: A config file is necessary for the program to run, however, this file may be empty, in which case the
default values of the simulation will be used to provide a simulation. 

### Configuration file
Configuration file name is provided via CLA. 
**Prebuilt configuration files include:
gridConfig, randomConfig, ranGridConfig which can be found in the resources folder**.
Parameters are labeled in the first column of config file 
for ease of updating. Parameters are in the following order (though parameters may be in any order):
 1. grid r c / randomGrid r c n / random n
 2. dimensions r c
 3. distance n
 4. incubation n
 5. sickness n
 6. recover n
 7. initialsick n 
 8. immune n

In the case of missing parameters the defaults are as follows:
 1. random 100
 2. dimensions 200 200
 3. incubation 5
 4. sickness 10
 5. recovery 95
 6. random 100
 7. initialsick 1
 8. immune 5

### Implemented features
    - Initial immune agents to config
    - Agent logging
    - Restart simulation from client

### Known Bugs

 - No known bugs or issues. 

### Contributions
The following is a simplified list of contributions made by each project contributor.

#### Cyrus:

- Config file parsing
- Test configs
- Object Constructors

#### Jonathan:

- Object display representation
- Agent scaling for display
- Agent initialization dependent on simulation type.

#### Both:

- Concurrent execution of agent tasks.
- Display design and debugging.
- Primary simulation loop.