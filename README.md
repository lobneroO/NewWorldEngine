# README #

Source code for the New World Engine by New World Laboratories.
For now this is a practice environment to learn "new" OpenGL functionalities and game development.
This work is done next to my Master's Thesis, thus updates can't be as frequent as I want them to be.

### What is this repository for? ###

* Basic Rendering and Game Engine by New World Laboratories (i.e. Tim Lobner). Currently only loading and displaying of models with or without textures and a basic 3rd person camera for a player entity, that you can move around.
* version 0.1

### How do I get set up? ###

* Set Up: Download the source code and set up the project in your IDE. Set up JOGL (i.e. the jogl-all.jar, the gluegen-rt.jar and the two jars specified for your OS have to be added to the project) and JOML (i.e. the joml-1.9.2.jar has to be added to the project) as libraries and you should be good to go. The JOGL and JOML dependencies have not been tested for other versions, so no guarantees outside of the mentioned versions.
* Configuration: -
* Dependencies: 
- JOGL v 1.1.1. https://jogamp.org/wiki/index.php/Downloading_and_installing_JOGL
- JOML v 1.9.2. http://joml-ci.github.io/JOML/
* Database configuration: -
* How to run tests: -
* Deployment instructions: -

### Contribution guidelines ###

* Writing tests:- OpenGL does not have any true testing in its output. The testing implemented is a check up on initialization of new objects on whether everything was fine when initializing.
* Code review:- Seeing as this is a one man project, there is no special code review other than developing, checking for success, trying to get the code readable and commiting.
* Other guidelines: - While this is a java project, C++ writing style is prefered (e.g. brackets for functions, loops and so forth have their own lines).

### Who do I talk to? ###

* Repo owner and admin: Tim Lobner, lobner@mail.upb.de
* Other community or team contact: This is a one man project thus far. Apart from the usual benefits of source code version control, it is meant for applications to show understanding of git and coding in general, as well as for demo reels (although they will be exported for quick use in all instances as well).