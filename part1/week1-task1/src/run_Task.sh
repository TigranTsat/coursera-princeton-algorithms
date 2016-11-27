#!/bin/bash -e

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

target_bin="${DIR}/../bin/"
javac-algs4 -d "${DIR}/../bin/" PercolationStats.java
echo "PercolationStats.java was compiled"
# It's better not specify cp, since it's overriden by java-algs4 script
(set +x && cd "${target_bin}" && java-algs4 PercolationStats 100 200)

echo "____ COMPLETED ____"
