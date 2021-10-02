# N Queens

A purely frontend implementation of the N Queens Problem

## Overview

A purely frontend implementation of the N Queens Problem with clojurescript.
<img width="1102" alt="屏幕快照 2021-10-02 上午10 14 07" src="https://user-images.githubusercontent.com/4686316/135700710-334038aa-3d3b-4523-bb18-b31b55e8a552.png">


## Setup

To get an interactive development environment run:


    lein figwheel

and open your browser at [localhost:3449](http://localhost:3449/).
This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to try it is:

    (js/alert "Am I connected?")

and you should see an alert in the browser window.

To clean all compiled files:

    lein clean

To create a production build run:

    lein do clean, cljsbuild once min

And open your browser in `resources/public/index.html`. You will not
get live reloading, nor a REPL. 

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
