## Goal

This is an attemp to make a polyglot web server with minimal configuration

## Getting Started

### Building
Open-Cloud depends on GraalVM for Polyglot support, refer to [their website](https://www.graalvm.org/) for installing instruction.

After installed GraalVM (and add it to your $PATH variable), run `./gradlew shadowJar` or alternatively, you can get the prebuilt .jar here.

### Your first function
TODO

## Features

* [x] Support JVM languages, R, Python, JavaScript, Ruby, and LLVM (executing LLVM is unsafe when using GraalVM CE)
* [ ] Support running WebAssembly (with Asmble, WAVM?)
* [x] Simple POST, GET
* [ ] Functions interaction
* [ ] Share state between functions (support sessions,...)
* [ ] Predefined common components (rate-limiting, authentication...)
* [ ] Horizontal scale
* [ ] Dashboard

_(Help needed)_

## Performance

Speed is acceptable I guess. This is a micro-benchmark

TODO
## Contributing
Open issues if you have any question and please tell me if you actually use this to build something.

[![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=C44YKYMVNL4TA)
