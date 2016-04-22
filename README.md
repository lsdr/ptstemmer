# PTStemmer - A Stemming toolkit for the Portuguese language

## Features

* Python implementations of Orengo, Porter, and Savoy stemmers
* Fast: can stem more than 1.5M words/second on a normal desktop
* Least Recently Used (LRU) stem cache
* Support for lists of words to ignore (useful for stopword and named entity removal)

## About the Project

The project was originally developed by [Pedro Oliveira](http://www.cpdomina.net).

This is a fork automatically exported from the Google Code original repos that
lived at [code.google.com/p/ptstemmer](https://code.google.com/archive/p/ptstemmer/).

The original project contained also Java and C# implementations of the stemmers,
but I removed since I had no interested in them. I have the original code tagged
under `original-export` and can be retrieved with a simple checkout:

```sh
$ git checkout original-export
```

## Licensing

The original work, and therefore this fork, are licensed under the [GNU Lesser
General Public License](http://www.gnu.org/licenses/lgpl.html),
[version 3.0](http://choosealicense.com/licenses/lgpl-3.0/) (LGPLv3).

A verbatim copy of the license can be found in the LICENSE file.

