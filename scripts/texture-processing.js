"use strict"
function die() { alert.apply(null, arguments); exit(1); }
const strip_images = false;
const image_infos  = true;

if(sys.args.length !== 1) die("specify a base directory where to strip png files in.");
const basedir = fs.realpath(sys.args[0]);
if(!fs.isdir(basedir)) die("Base directory does not exist: '" + sys.args[0] + "'");
const imagick_ok = sys.shell("magick --help").toLowerCase().replace(/[\s]/ig,"").search("usage:") == 0; // brief check
if(!imagick_ok) die("The 'magick' command seems not to be in the executable PATH, or not to be compatible.");
var files = fs.find(basedir, '*.png');
print("[note ] " + (files.length) + " files to strip.");

for(var it in files) {
  if(image_infos) {
    var stdo = sys.shell('magick identify "' + files[it] + '"');
    stdo = "[ident] " + stdo.replace(basedir,"").replace("\\", "/").replace(/^[\s\/]+/,"").replace(/[\s]+$/,"");
    print(stdo);
  }
  if(strip_images) {
    var stdo = sys.shell('magick mogrify -verbose -strip "' + files[it] + '"');
    stdo = "[strip] " + stdo.replace(basedir,"").replace("\\", "/").replace(/^[\s\/]+/,"").replace(/[\s]+$/,"");
    print(stdo);
  }
}
print("[done ]");
