# README #

Source code for the New World Engine by New World Laboratories.
For now this is a practice environment to learn "new" OpenGL functionalities and game development.
This work is done next to my Master's Thesis, thus updates can't be as frequent as I want them to be.

An overview over the changes in each version can be found at the bottom.

### What is this repository for? ###

* Basic Rendering and Game Engine by New World Laboratories (i.e. Tim Lobner). 
Currently available features and techniques (i.e. all features that can be used in the master branch, may not necessarily be used in the release branches which are more of a demo thing and don't contain unused code for data size sake): 
-loading and displaying of models from obj files with textures
-basic 3rd person camera for a player entity, that you can move around.
-basic player movement
-terrains with multi texturing
-rendering copied entities efficiently
-basic lighting
* Version 0.2 (released on May 21st 2017)
* Version 0.1 (released on May 4th 2017)
* Download of an executable jar file at https://bitbucket.org/tlobner/new-world-engine/downloads
Choose the file that suits your OS, extract the archive and run the jar. If you encounter problems, let me know via lobner@mail.upb.de

### How do I get set up? ###

* Set Up: Download the source code and set up the project in your IDE. Set up JOGL  and JOML as libraries and you should be good to go. The JOGL dependency is currently JOGL 2.3.2 (except branch release_0.1, which uses JOGL 1.1.1). The JOML dependency have not been tested for other versions, so no guarantees outside of the mentioned version.
* Configuration: 
- the jogl-all.jar, the gluegen-rt.jar and the two jars specified for your OS have to be added to the project
- the joml-1.9.2.jar has to be added to the project
* Dependencies: 
- JOGL v 2.3.2. https://jogamp.org/wiki/index.php/Downloading_and_installing_JOGL
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

### Changes and additions in version 0.3 ###
* Supports cubemaps
* Supports a skybox
* Supports flipping TextureData and thus flipping a Texture at load time
* Supports height maps for the terrain and colision detection as well as correct positioning of the entities according to the terrain height

### Changes and additions in version 0.2 ###
* Update to JOGL 2.3.2 (not fully downards compatible)
* Runs with JOML 1.9.2, no guarantees for other versions
* Fixed the keyboard input
* A MasterRenderer to manage all individual Renderers to come
* Change the old Renderer to EntityRenderer and change the camera management and projection set up to the MasterRenderer
* Rendering entities in batches of equal entities (i.e. a batch contains every instance of an entity with the same texture and raw model) rather than every entity individually
* Enabled backface culling by default, can be turned off for TexturedModels by setting the hasTransparency flag
* Support fully transparent texels (i.e. they get discarded if the alpha value is less than 0.5)
* Addition of TerrainRenderer, TerrainShader and Terrain classes (instead of rendering the Terrain as an Entity)
* Support multiple textures for the terrain as well as a blend map
* Support loading obj models with multiple spaces (or tabulator symbols) in the obj file
* Support obj models with vertices being used in several vertex/texcoord/normal combinations in the face data
* Support obj files with quads as face data
* Support obj files that have unstructured vertex, texture coordinates, normal and face data (i.e. having a v entry, an f entry and then a v entry again)
* Support mirroring the player model / third person camera for the player model (i.e. if the model faces you due to the way it is modeled, one can set a mirrored flag which turns the camera 180° around the player and inverts the forwards and backwards speeds, thus W is running to the front of the model)


### SetUp version 0.1 ###
Version 0.1 runs with JOGL 1.1.1 and is not fully upwards compatible, since there was a package renaming.
Version 0.1 runs with JOML 1.9.2, no guarantees for other versions