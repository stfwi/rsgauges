#!/usr/bin/djs
if((!fs.chdir(fs.dirname(fs.realpath(sys.script))+"/..")) || (!fs.isdir(".git"))) throw new Error("Failed to switch to mod source directory.");

const uncommitted_changes = sys.shell("git status -s").trim();

const gittags = sys.shell('git log -1 --format="%D"')
                .replace(/[\s]/g,"").split(",")
                .filter(function(s){ return s.indexOf("tag:")==0;})
                .map(function(s){ return s.replace(/^tag:/,"");});

const version_rsgauges = fs.readfile("gradle.properties", function(line){
  if(line.trim().indexOf("version_rsgauges")!=0) return false;
  return line.replace(/^.*?=/,"").trim()
}).trim();

const git_remote = sys.shell("git remote -v").trim();
const git_branch = sys.shell("git rev-parse --abbrev-ref HEAD").trim();
var fails = [];

if(version_rsgauges=="") fails.push("Could not determine 'version_rsgauges' from gradle properties.");
if(!gittags.length) fails.push("Version not tagged.");
if(!gittags.filter(function(s){return s.indexOf(version_rsgauges.replace(/[-]/g,""))>=0}).length) fails.push("No tag version not found matching the gradle properties version.");
if(git_remote.replace(/[\s]/g,"").indexOf("git@github.com:stfwi/rsgauges.git(push)") < 0) fails.push("Not the reference repository.");
if((git_branch != "develop") && (git_branch != "master")) {
  fails.push("No valid branch for dist. (branch:'"+git_branch+"')");
} else if((git_branch == "develop") && (version_rsgauges.replace(/[^\w\.-]/g,"")=="")) {
  fails.push("Cannot make release dist on develop branch.");
} else if((git_branch == "master") && (version_rsgauges.replace(/[^\w\.-]/g,"")!="")) {
  fails.push("Cannot make beta dist on master branch.");
}

if((!fs.isfile("signing.jks")) || (!fs.isfile("signing.properties"))) fails.push("Jar signing files missing.");

if(fails.length>0) {
  for(var i in fails) fails[i] = "  - " + fails[i];
  alert("Dist check failed");
  alert(fails.join("\n")+"\n");
  exit(1);
}
