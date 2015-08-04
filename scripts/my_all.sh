#!/bin/sh

f="$1.i";
g=$1
g1="${g}1"
g2="${g}2"
g3="${g}3"

python haiguan_norm.py $g $f


awk -F "\t" '{print $4}' $f > $g
python haiguan_brand.py $g $g1 "name"; 

awk -F'[\t\"]+' '{print $5}' $f > $g;
python haiguan_brand.py $g $g2 "model";

paste $g1 $g2 > $g3

awk -F "\t" '{if($1=="\\n")brand=$5;else brand=$1;if($2=="\\n")model=$6;else model=$2;printf("%s\t%s\t%s\t%s\n",brand,model,$3,$4)}' $g3 > $g1

paste $f $g1 > $g2;
python haiguan_commodity.py $g2 $g

rm -f $f $g1 $g2 $g3