# Starsight
A rendering engine for low-poly or voxel graphics.

## Purpose
- To allow easy low-setup low-poly/voxel graphics in an optimized fashion
- To provide a light abstraction over OpenGL in such a manner as to allow OpenGL beginners to tinker without learning another engine
- To create the look and feel for a video game called Starcave

## Capabilities
The capabilities of this engine are rather simple: You can create a mesh, create a shader, send uniforms to the GPU, and render it, all while receiving inputs.

The Window class can be set up in a separate thread. Note that this engine is far from Thread-safe, and should not be used in concurrent environments unless the Window is strictly quarantined from non-rendering functions.