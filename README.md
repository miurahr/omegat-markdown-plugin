# Markdown filter plugin for OmegaT

This is an incomplete implementation of OmegaT plugin which support Markdown document for translation.

## Install

Please download zip file from Github release. You can get jar file from zip distribution.
OmegaT plugin should be placed in `$HOME/.omegat/plugin` or `C:\Program Files\OmegaT\plugin`
depending on your operating system.

## Features

It can do:

- Load Markdown source file.

- Generate translated file.


## Known issues

- It cannot treat inline components properly such as **Emphasis** and  [link](http://example.com).
  You may find these components as individual sentenses instead of a part of sentense..


- It cannot treat table elements properly.

## License

This project is distributed under the GNU general public license version 3 or later.


