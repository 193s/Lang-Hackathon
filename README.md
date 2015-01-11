# Marg
A JVM-based scripting language.

******

Language Specification(Japanese): <https://github.com/193s/marg/wiki/Language-Specification>

## Usage
```sh
$ git clone https://github.com/193s/marg.git
$ cd marg
$ sbt run
```

******


###### Example
```
var a = 0
while (a < 10):
    echo a
    a = a+1
;

```
