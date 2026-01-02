# SipHash
[![Build Status](https://img.shields.io/github/actions/workflow/status/whitfin/siphash-java/ci.yml?branch=main)](https://github.com/whitfin/siphash-java/actions) [![Coverage Status](https://img.shields.io/coveralls/whitfin/siphash-java.svg)](https://coveralls.io/github/whitfin/siphash-java) [![Maven Version](https://img.shields.io/maven-central/v/io.whitfin/siphash-java.svg)](https://central.sonatype.com/artifact/io.whitfin/siphash) [![Documentation](https://img.shields.io/badge/docs-latest-blue.svg)](https://www.javadoc.io/doc/io.whitfin/siphash)

A Java implementation of the SipHash cryptographic hash family. Supports any variation, although defaults to the widely used SipHash-2-4. This library offers both a zero-allocation implementation, along with a streaming digest.

This library was heavily influenced by [veorq's C implementation](https://github.com/veorq/siphash) and [Forward C&C's reference implementation](http://www.forward.com.au/pfod/SipHashJavaLibrary/) - I just decided it was time a Java implementation of SipHash made it onto Maven :).

## Setup

`siphash` is available on Maven central, via Sonatype OSS:

```xml
<dependency>
    <groupId>io.whitfin</groupId>
    <artifactId>siphash</artifactId>
    <version>3.0.0</version>
</dependency>
```

## Usage

There are three main ways to use this library, and the appropriate choice will depend on your use case. For further usage, please visit the [documentation](http://www.javadoc.io/doc/io.whitfin/siphash).

### Zero Allocation

The fastest use of this algorithm is to simply call `SipHasher.hash/2` which will call a zero-allocation implementation of the SipHash algorithm. This implementation should be used in most cases; specifically cases where you have frequently differing seed keys.

```java
import io.whitfin.siphash.SipHash;

// specify the key and data pair
String key = "0123456789ABCDEF".getBytes();
String data = "my-input".getBytes();

// hash using default compression (2-4)
long hash1 = SipHash.hash(key, data);

// you can also specify compression rounds
long hash2 = SipHash.hash(key, data, 2, 4);
```

### Contained Hashing

This is an optimized implementation for cases where you have a single key (such as a hash table). In these cases, the seed values can be precomputed and re-used, rather than calculating them repeatedly on each call to hash. Although the initial call to create a container uses an allocation, there are no other allocations inside the container.

```java
import io.whitfin.siphash.SipHash;
import io.whitfin.siphash.SipHashContext;

// create a container from our key
String key = "0123456789ABCDEF".getBytes();
SipHashContext ctx = SipHasher.context(key);

// hash using default compression (2-4)
long hash1 = ctx.hash(data);

// you can also specify compression rounds
long hash2 = ctx.hash(data, 2, 4);
```

### Streaming Digest

The final way to use the library is as a streaming digest; meaning that you can apply chunks of input as they become available. The advantage here is that you can hash input of unknown length. Naturally, this is slower than the alternatives and should only be used when necessary. A digest cannot be re-used; one must be created on a per-hash basis.

```java
// create a container from our key
String key = "0123456789ABCDEF".getBytes();
SipHashStream hash = SipHasher.init(key);

// update several times
hash.update("chu".getBytes());
hash.update("nked".getBytes());
hash.update(" string".getBytes());

// retrieve the final result
long result = hash.digest();
```

## Formatting

By default, as of v2.0.0, all hashes are returned as a `long`. However, you can use `SipHasher.toHexString/1` to convert a hash to a hexidecimal String value.

```java
// output will be padded (if necessary) to 16 bytes
SipHash.toHexString(-3891084581787974112L); // ca0017304f874620
SipHash.toHexString(   77813817455948350L); // 011473413414323e
```

## Contributing

Tests are run using `mvn`. I aim to maintain 100% coverage where possible (both line and branch):

```bash
$ mvn clean verify
```

If you wish to contribute (awesome!), please file an issue first! All PRs should pass `mvn clean verify` and maintain 100% test coverage.
