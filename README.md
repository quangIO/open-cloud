## Goal

This is an attemp to make a polyglot, minimal configure web server

## Getting Started

### Building
Open-Cloud depends on GraalVM for Polyglot support, refer to [their website](https://www.graalvm.org/) for installing instruction.

After installed GraalVM (and add it to your $PATH variable), run `./gradlew shadowJar` or alternatively, you can get the prebuilt .jar here.

### Your first function
TODO

## Features

* [x] Support Java, R, Python, JavaScript, Ruby, and LLVM (this is unsafe when running GraalVM CE edition)
* [ ] Support running WebAssembly (with Asmble, WAVM?)
* [x] Simple POST, GET
* [ ] Functions interaction
* [ ] Share state between functions
* [ ] Predefined common components
## Performance

Speed is acceptable I guess. This is the micro-benchmark

TODO
## Contributing
Open issues if you have any question and please tell me if you actually use this to build something.
