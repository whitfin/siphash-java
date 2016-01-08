# SipHash

[![Build Status](https://travis-ci.org/zackehh/siphash-java.svg?branch=master)](https://travis-ci.org/zackehh/siphash-java)

A Java implementation of the SipHash cryptographic hash family. Supports any variation, although defaults to the widely used SipHash-2-4. Can be used with either full input, or used as a streaming digest.

This library was heavily influenced by [veorq's C implementation](https://github.com/veorq/siphash) and [Forward C&C's reference implementation](http://www.forward.com.au/pfod/SipHashJavaLibrary/) - I just decided it was time a Java implementation of SipHash made it onto Maven :).