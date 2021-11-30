#!/usr/bin/env sh

# Execute this command in the root directory
# needs uncrustify to be installed
# try:
# $ brew install uncrustify
find ./xcode/Jasonette -name "*.h" | xargs uncrustify -c ./tools/uncrustify/uncrustify.cfg --replace --no-backup
find ./xcode/Jasonette -name "*.m" | xargs uncrustify -c ./tools/uncrustify/uncrustify.cfg --replace --no-backup