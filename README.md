# Marg
A JVM-based scripting language.
  
## Marg
An interpreter of Marg written in Java.

  
<!--
******

# Installation
```
$ wget http://github.com/193s/marg
$ mkdir ~/.marg/
$ export PATH="$PATH:$HOME/.marg/bin"
```

# How to Use
```
$ imr
``` -->

******

# Language Documentation
  
###### Example
```
a = 0,
while (a < 10):
    a = a+1
;
 
```
  
  
### Comment
```
# comment

#-- comment
comment*
--#
```

### Statement
Now you can use If and While statement like this:  
```
Reserved (Condition):
    Statements
;
```

For example:
```
var a = 1000
if (a == 0):
  echo a
;
```






