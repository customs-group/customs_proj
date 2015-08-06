#!/bin/sh

f="$1.i";
g=$1
tmp="${g}_tmp"
ready="${g}_ready"
out="${g}_out"

gname="${g}_name"
gmodel="${g}_model"

python haiguan_norm.py $g $f

#1. sub 1""->1##
#2. sub """->"
#3. sub ""->null
#4. contat gname@@gmodel
#4. sub back 1##->1""
#5. sub xxx型号xxx->xxx,型号xxx
sed 's/\([0-9]\)\"\"/\1##/g' $f | awk '{gsub(/[\"]{3}/,"\"");print $0}' | awk '{gsub(/\"{2}/,"");print $0}' > tmp

awk -F'[\t\"]+' '{print $3"@@"$4}' tmp | awk '{gsub(/##/,"\"\"");print $0}' | sed 's/\([^\s,;:\|\(@]\)\(型号\)/\1,\2/' > ready

python data_process.py ready out

#python haiguan_brand.py $g $g1 "name"; 

#python haiguan_brand.py $g $g2 "model";


paste $g1 $g2 > $g3

awk -F "\t" '{if($1=="\\n")brand=$5;else brand=$1;if($2=="\\n")model=$6;else model=$2;printf("%s\t%s\t%s\t%s\n",brand,model,$3,$4)}' $g3 > $g1

paste $f $g1 > $g2;
python haiguan_commodity.py $g2 $g

rm -f $f $g1 $g2 $g3