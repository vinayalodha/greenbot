#! /bin/bash
openssl speed -multi $(grep -ci processor /proc/cpuinfo)
