# Starsight
A rendering engine for low-poly or voxel graphics.

## Purpose
- To allow easy low-setup low-poly/voxel graphics in an optimized fashion
- To provide a light abstraction over OpenGL in such a manner as to allow OpenGL beginners to tinker without learning another engine
- To create the look and feel for a video game called Starcave
 
## Capabilities
The capabilities of this engine are rather simple: You can create a mesh, create a shader, send uniforms to the GPU, and render it, all while receiving inputs.

The Window class can be set up in a separate thread. Note that this engine is far from Thread-safe, and should not be used in concurrent environments unless the Window is strictly quarantined from non-rendering functions.

## Task list

### 1.0.0a
- [x] Mesh Loading and Drawing
- [x] Shader compilation and binding
- [x] Texture loading and binding
- [x] Camera creation with mouse control
- [x] Input processing and callbacks
- [x] Tesselator
### 1.1.0a
- [x] Framebuffer creation and presentation
    - [ ] Test it
- [x] BufferedImage based texture class
    - [X] Fix it
- [x] Texture Atlasing
    - [ ] Test it
### 1.2.0a
- [ ] Shader preprocessors
- [ ] Buffer based mesh loading
- [ ] Quad culling techniques
- [ ] Lightmaps
- [ ] Multiple types of tesselators
### 1.3.0a
- [ ] Extensive logging
- [ ] Integrate profiler from CoreUtils (To be built in CoreUtils)
- [ ] Animated textures
### 1.4.0a
- [ ] Transparency
- [ ] Text rendering
### 2.0a
- [ ] UI system
- [ ] Layout managers
- [ ] Text rendering
- [ ] Clicking the elements :D
### 2.1a
- [ ] Audio integration from OpenAL

### 1.0.0b
- [ ] To-Javadoc audit
- [ ] Bug audit
- [ ] Refactorings audit
- [ ] New features audit
### 1.1.0b
- [ ] Javadoc additions

### 1.0.0
- [ ] A stable build packaged up into a neat JAR file with all the fixings
