# SipHash

[![Build Status](https://travis-ci.org/whitfin/siphash-java.svg?branch=master)](https://travis-ci.org/whitfin/siphash-java) [![Coverage Status](https://coveralls.io/repos/whitfin/siphash-java/badge.svg?branch=master&service=github)](https://coveralls.io/github/whitfin/siphash-java?branch=master)

A Java implementation of the SipHash cryptographic hash family. Supports any variation, although defaults to the widely used SipHash-2-4. This library offers both a zero-allocation implementation, along with a streaming digest.

This library was heavily influenced by [veorq's C implementation](https://github.com/veorq/siphash) and [Forward C&C's reference implementation](http://www.forward.com.au/pfod/SipHashJavaLibrary/) - I just decided it was time a Java implementation of SipHash made it onto Maven :).

## Setup

`siphash` is available on Maven central, via Sonatype OSS:

```
<dependency>
    <groupId>io.whitfin</groupId>
    <artifactId>siphash</artifactId>
    <version>2.0.0</version>
</dependency>
```

## Usage

There are three main ways to use this library, and the appropriate choice will depend on your use case. For further usage, please visit the [documentation](http://www.javadoc.io/doc/io.whitfin/siphash).

### Zero Allocation

The fastest use of this algorithm is to simply call `SipHasher.hash/2` which will call a zero-allocation implementation of the SipHash algorithm. This implementation should be used in most cases; specifically cases where you have frequently differing seed keys.

```java
// specify the key and data pair
String key = "0123456789ABCDEF".getBytes();
String data = "my-input".getBytes();

// hash using default compression (2-4)
long hash1 = SipHasher.hash(key, data);

// you can also specify compression rounds
long hash2 = SipHasher.hash(key, data, 2, 4);
```

### Contained Hashing

This is an optimized implementation for cases where you have a single key (such as a hash table). In these cases, the seed values can be precomputed and re-used, rather than calculating them repeatedly on each call to hash. Although the initial call to create a container uses an allocation, there are no other allocations inside the container.


```java
// create a container from our key
String key = "0123456789ABCDEF".getBytes();
SipHasherContainer container = SipHasher.container(key);

// hash using default compression (2-4)
long hash1 = container.hash(data);

// you can also specify compression rounds
long hash2 = container.hash(data, 2, 4);
```

### Streaming Digest

The final way to use the library is as a streaming digest; meaning that you can apply chunks of input as they become available. The advantage here is that you can hash input of unknown length. Naturally, this is slower than the alternatives and should only be used when necessary. A digest cannot be re-used; one must be created on a per-hash basis.

```java
// create a container from our key
String key = "0123456789ABCDEF".getBytes();
SipHasherStream hash = SipHasher.init(key);

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
SipHasher.toHexString(-3891084581787974112L); // ca0017304f874620
SipHasher.toHexString(   77813817455948350L); // 011473413414323e
```

## Contributing

If you wish to contribute (awesome!), please file an issue first! All PRs should pass `mvn clean verify` and maintain 100% test coverage.

## Testing

Tests are run using `mvn`. I aim to maintain 100% coverage where possible (both line and branch).

Tests can be run as follows:

```bash
$ mvn clean verify
```
