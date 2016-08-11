while read p; do yes|unzip -P $p legendaryPokemon.zip 2>/dev/null | awk '{if (NR>1) print}'; done < dictionary.txt
