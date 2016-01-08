# SipHash

[![Build Status](https://travis-ci.org/zackehh/siphash-java.svg?branch=master)](https://travis-ci.org/zackehh/siphash-java) [![Coverage Status](https://coveralls.io/repos/zackehh/siphash-java/badge.svg?branch=master&service=github)](https://coveralls.io/github/zackehh/siphash-java?branch=master)

A Java implementation of the SipHash cryptographic hash family. Supports any variation, although defaults to the widely used SipHash-2-4. Can be used with either full input, or used as a streaming digest.

This library was heavily influenced by [veorq's C implementation](https://github.com/veorq/siphash) and [Forward C&C's reference implementation](http://www.forward.com.au/pfod/SipHashJavaLibrary/) - I just decided it was time a Java implementation of SipHash made it onto Maven :).

## Setup

`siphash` is available on Maven central, via Sonatype OSS:

```
<dependency>
    <groupId>com.zackehh</groupId>
    <artifactId>siphash</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Usage

There are two ways of using SipHash (see below). Both return a `SipHashResult` which can be used to retrieve the result in various forms. All constructors can take arguments to specify the compression rounds. For further usage, please visit the [documentation](http://www.javadoc.io/doc/com.zackehh/siphash-java).

#### Full Input Hash

The first is to simple create a `SipHash` instance and use it to repeatedly hash using the same key.

The internal state is immutable, so you can hash many inputs without having to recreate a new `SipHash` instance (unless you want a new key).

```java
SipHash hasher = new SipHash("0123456789ABCDEF".getBytes());

SipHashResult result = hasher.hash("my-input".getBytes());

System.out.println(result.get());                             // 182795880124085484 <-- this can overflow
System.out.println(result.getHex());                          //  "2896be26d3374ec"
System.out.println(result.getHex(true));                      // "02896be26d3374ec"
System.out.println(result.getHex(SipHashCase.UPPER));         //  "2896BE26D3374EC"
System.out.println(result.getHex(true, SipHashCase.UPPER));   // "02896BE26D3374EC"
```

#### Streaming Hash

The second is to use the library as a streaming hash, meaning you can apply chunks of bytes to the hash as they become available.

Using this method you must create a new digest every time you want to hash a different input as the internal state is mutable.

```java
SipHashDigest digest = new SipHashDigest("0123456789ABCDEF".getBytes());

digest.update("chu".getBytes());
digest.update("nked".getBytes());
digest.update(" string".getBytes());

SipHashResult result = digest.finish();

System.out.println(result.get());                             // 3502906798476177428 <-- this can overflow
System.out.println(result.getHex());                          //  "309cd32c8c793014"
System.out.println(result.getHex(true));                      //  "309cd32c8c793014"
System.out.println(result.getHex(SipHashCase.UPPER));         //  "309CD32C8C793014"
System.out.println(result.getHex(true, SipHashCase.UPPER));   //  "309CD32C8C793014"
```

## Contributing

If you wish to contribute (awesome!), please file an issue first! All PRs should pass `mvn clean verify` and maintain 100% test coverage.

## Testing

Tests are run using `mvn`. I aim to maintain 100% coverage where possible (both line and branch).

Tests can be run as follows:

```bash
$ mvn clean verify
```