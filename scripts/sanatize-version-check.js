#!/usr/bin/djs
"use strict";

if((!fs.chdir(fs.dirname(fs.realpath(sys.script))+"/..")) || (!fs.isdir("src"))) throw new Error("Failed to switch to mod base directory.");

var modrsgauges_java = fs.readfile("src/main/java/wile/rsgauges/ModRsGauges.java", function(line){
  return line.search(/String[\s]+MODVERSION[\s]*=/i) >= 0;
}).replace(/^.*?"/,"").replace(/".*/,"").trim();

var build_gradle = fs.readfile("build.gradle", function(line){
  return line.search(/^version[\s]*=[\s]*"/i) >= 0;
}).replace(/^.*?"/,"").replace(/".*/,"").trim();

var mcmod_info = JSON.parse(fs.readfile("src/main/resources/mcmod.info"));
const mc_version = (mcmod_info[0].mcversion.trim());
const mod_version = (mcmod_info[0].version.trim());
mcmod_info = "mc" + mc_version + "-" + mod_version;


var readme_version_found = fs.readfile("readme.md", function(line){
  var m = line.match(/^[\s]+-[\s]+v([\d]+[\.][\d]+[\.][\d]+[abrc][\d]+)/i);
  if((!m) || (!m.length) || (m.length < 2)) return false;
  return m[1]==mod_version;
});

var ok = true;
if((modrsgauges_java != mcmod_info) || (build_gradle != mcmod_info)) {
  alert("Version data differ.");
  ok = false;
}
if(!readme_version_found) {
  alert("Version '"+mod_version+" ' not found in the readme changelog.");
  ok = false;
}
if(!ok) {
  alert("Version data:");
  alert(" - mcmod.info      : '" + mcmod_info + "'");
  alert(" - build.gradle    : '" + build_gradle + "'");
  alert(" - ModRsGauges.java: '" + modrsgauges_java + "'");
  exit(1);
}
