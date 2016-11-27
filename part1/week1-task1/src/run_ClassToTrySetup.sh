#!/bin/bash -e

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

javac_algs_loc=$(which javac-algs4)
java_algs_loc=$(which java-algs4)
echo "Found javac-algs = '${javac_algs_loc}' and java-algs = '${java_algs_loc}"

target_bin="${DIR}/../bin/"
javac-algs4 -d "${DIR}/../bin/" ClassToTrySetup.java
echo "ClassToTrySetup was compiled"
# It's better not specify cp, since it's overriden by java-algs4 script
(cd "${target_bin}" && java-algs4  ClassToTrySetup )

echo "____ COMPLETED ____"
