# README #

Source code for the New World Engine by New World Laboratories.
For now this is a practice environment to learn "new" OpenGL functionalities and game development.
This work is done next to my Master's Thesis, thus updates can't be as frequent as I want them to be.

### What is this repository for? ###

* Basic Rendering and Game Engine by New World Laboratories (i.e. Tim Lobner). 
Currently available features and techniques (i.e. all features that can be used in the master branch, may not necessarily be used in the release branches which are more of a demo thing and don't contain unused code for data size sake): 
-loading and displaying of models with or without textures 
-basic 3rd person camera for a player entity, that you can move around.
* Version 0.1 (released on May 4th 2017)
* Download of an executable jar file at https://bitbucket.org/tlobner/new-world-engine/downloads
Choose the file that suits your OS, extract the archive and run the jar. If you encounter problems, let me know via lobner@mail.upb.de

### How do I get set up? ###

* Set Up: Download the source code and set up the project in your IDE. Set up JOGL  and JOML as libraries and you should be good to go. The JOGL dependency is currently JOGL 1.1.1 and does not support all higher versions due to renaming of packages. The next update will use JOGL 2.3.2, as does the current source code. The JOML dependency have not been tested for other versions, so no guarantees outside of the mentioned version.
* Configuration: 
- the jogl-all.jar, the gluegen-rt.jar and the two jars specified for your OS have to be added to the project
- the joml-1.9.2.jar has to be added to the project
* Dependencies: 
- JOGL v 1.1.1. https://jogamp.org/wiki/index.php/Downloading_and_installing_JOGL
(not fully upwards compatible, will change to JOGL 2.3.2 in the next release, is already used in the current source code though)
- JOML v 1.9.2. http://joml-ci.github.io/JOML/
* Database configuration: -
* How to run tests: -
* Deployment instructions: 
- The master branch has to be runnable at all times!
- Public releases get their own release branch in the repository that are named "release_major.minor" where major and minor are the version numbers. The branch should not be updated once it is pushed to the repository, unless there are problems with running the files contained in the branch or files are missing. Before pushing the branch, delete all content that is not used in the runnable version. Once a release branch is pushed to the repository, create executable versions of the program named and archive them as "NWEmajor.minor_OSNAMEbit.zip", where major and minor are the version numbers, OSNAME is the respective name of the OS it is for (win, mac, linux) and bit is the bit version of the OS, if applicable (e.g. NWE0.1_win64bit.zip, NWE0.1_mac.zip). Upload the files to this repository's download section.

### Contribution guidelines ###

* Writing tests:- OpenGL does not have any true testing in its output. The testing implemented is a check up on initialization of new objects on whether everything was fine when initializing.
* Code review:- Seeing as this is a one man project, there is no special code review other than developing, checking for success, trying to get the code readable and commiting.
* Other guidelines: - While this is a java project, C++ writing style is prefered (e.g. brackets for functions, loops and so forth have their own lines).

### Who do I talk to? ###

* Repo owner and admin: Tim Lobner, lobner@mail.upb.de
* Other community or team contact: This is a one man project thus far. Apart from the usual benefits of source code version control, it is meant for applications to show understanding of git and coding in general, as well as for demo reels (although they will be exported for quick use in all instances as well).