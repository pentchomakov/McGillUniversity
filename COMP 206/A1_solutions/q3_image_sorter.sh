#Pentcho Tchomakov 260632861

if [ ! -d "$1" ]; then
    echo "The specified directory does not exist"
    exit
fi

filename="$1"".jpg"

>$filename
for file in `find $1 -type f -print|xargs ls -t`
do
    convert $file $filename -append $filename
done
display ./ $filename

