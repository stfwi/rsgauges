#!/usr/bin/djs
// Note for reviewers/clones: This file is a auxiliary script for my setup. It's not needed to build the mod.
"use strict";

(function(constants){
  const me = {'tasks':{}, 'parsing':{},'sanatizing':{}};

  /**
   * Returns the version history as full text from a given file path.
   * @returns {string}
   */
  me.parsing.readme_history_section = function (file_path) {
    var readme = fs.readfile(file_path);
    if(!readme) throw new Error("Failed to load readme.md");
    readme = readme.split(/[\r]?[\n]/);
    while((readme.length > 0) && readme[0].search(/^## Version history/i)<0) readme.shift();
    while((readme.length > 0) && readme[0].trim()=="") readme.shift();
    // version history section
    if(!readme.length) throw new Error("Version history section not found in readme");
    readme.shift();
    var end_of_history = readme.length;
    for(var i=0; i<readme.length; ++i) if(readme[i].search(/^---/) >= 0) { end_of_history=i; break; }
    if(end_of_history >= readme.length) throw new Error("Could not find the end-of-history header marker.");
    // remove empty lines, splitters
    while(readme.length >= end_of_history) readme.pop();
    while((readme.length >0) && (readme[readme.length-1].replace(/[\s-]/g,"")=="")) readme.pop();
    const min_indent = readme
      .map(function(s){return s.search(/[^\s]/)})
      .filter(function(e){return e>=0})
      .reduce(function(acc,e){return (e<acc)?e:acc});
    if(min_indent > 1) {
      for(var i in readme) { readme[i] = readme[i].substr(min_indent-2); }
    }
    return readme.join("\n");
  }

  /**
   * Returns the version history as array of version-text pairs from a given file path.
   * @returns {array}
   */
  me.parsing.readme_changelog = function(file_path) {
    var readme = me.parsing.readme_history_section(file_path).split(/[\r]?[\n]/);
    var versions = [];
    var ver="", txt=[];
    const addversion = function(){
      if((ver.length == 0) && (txt.length == 0)) return;
      if((ver.length > 0) != (txt.length > 0)) throw new Error("Version entry with empty corresponding text.");
      for(var i in txt) txt[i] = txt[i].trim();
      for(var i=txt.length-1; i>0; --i) {
        if((txt[i].length == 0) || (txt[i][0] == '[')) continue;
        txt[i-1] += " " + txt[i];
        txt[i] = "";
      }
      txt = txt.filter(function(v){return v.length>0;});
      for(var i in txt) txt[i] = txt[i].replace(/[\s]+/, " ");
      versions.push({ver:ver,txt:txt});
    };
    for(var il in readme) {
      var line = readme[il];
      if(line.replace(/[\s-]+/, "").length == 0) {
        continue; // separator line
      } if(line.search(/^[\s]*[~-][\s]*v[\d]+[\.][\d]+[\.][\d]/) == 0) {
        addversion();
        var is_preversion = (line.search(/^[\s]*[~]/) == 0);
        ver = line.replace(/^[\s]*[~-][\s]*/,"").replace(/[\s].*$/,"").toLowerCase();
        txt = [line.replace(ver, " ".repeat(ver.length)).replace(/^[\s]*[~-]/, function(m){ return " ".repeat(m.length); })];
        if(is_preversion) ver = "~" + ver;
      } else {
        txt.push(line);
      }
    }
    addversion();
    return versions;
  };

  /**
   * Returns the versions known from the readme
   * @returns {array}
   */
  me.parsing.readme_versions = function(file_path, no_pre_versions) {
    var o = me.parsing.readme_changelog(file_path);
    var versions = [];
    for(var i in o) versions.push(o[i].ver);
    if(!no_pre_versions) return versions;
    versions = versions.filter(function(v){ return v[0]!="~"; });
    return versions;
  }

  /**
   * Returns the gradle.properties settings as key-value plain object.
   * @returns {object}
   */
  me.parsing.gradle_properties = function(file_path) {
    var lines = fs.readfile(file_path).split(/[\r]?[\n]/);
    var properties = {};
    for(var i in lines) {
      var line = lines[i].trim();
      if(line.search("#")==0) continue;
      if(line.search("//")>=0) { line=line.substr(0, line.search("//")).trim(); }
      if(line.search(/^[a-z][a-z0-9_\.]+[=]/i) < 0) continue;
      line = line.split("=", 2);
      properties[line[0].trim()] = line[1].trim();
    }
    return properties;
  }

  /**
   * Returns an object containing the version data for MC, forge, the
   * mod, and the combined mod version.
   */
  me.parsing.version_data = function() {
    const properties = me.parsing.gradle_properties("gradle.properties");
    const version_minecraft = properties[constants.gradle_property_version_minecraft()];
    const version_forge = properties[constants.gradle_property_version_forge()];
    const version_mod = properties[constants.gradle_property_modversion()];
    const combined_version = version_minecraft + "-" + version_mod;
    return {
      minecraft: version_minecraft,
      forge: version_forge,
      mod: version_mod,
      combined: combined_version
    }
  };

  /**
   * Changes one tab to two spaces in files with the given extension
   * recursively found in the current working directory.
   * @returns {void}
   */
  me.sanatizing.tabs_to_spaces = function(extensions) {
    var file_list = (function() {
      var ls = [];
      const ext = extensions;
      if(fs.isdir("./src")) {
        for(var i in ext) ls = ls.concat(fs.find("./src", '*.'+ext[i]));
        for(var i in ls) ls[i] = ls[i].replace(/\\/g,"/");
      }
      ls.sort();
      ls.push("readme.md");
      return ls;
    })();
    for(var file_i in file_list) {
      var file = file_list[file_i];
      var txt = fs.readfile(file);
      if(txt===undefined) throw new Error("Failed to read '" + file + "'");
      const txt_length = txt.length;
      txt = txt.replace(/[\t]/g,"  ");
      const n = txt.length - txt_length;
      if(n > 0) {
        print("File '" + file + "': Changed " + n + " tabs to 2 spaces." );
        fs.writefile(file, txt);
      }
    }
  };

  /**
   * Removes space characters at the end of lines in files with the given
   * extension recursively found in the current working directory.
   * @returns {void}
   */
  me.sanatizing.remove_trailing_whitespaces = function(extensions) {
    var file_list = (function() {
      var ls = [];
      const ext = extensions;
      if(fs.isdir("./src")) {
        for(var i in ext) ls = ls.concat(fs.find("./src", '*.'+ext[i]));
        for(var i in ls) ls[i] = ls[i].replace(/\\/g,"/");
      }
      ls.sort();
      ls.push("readme.md");
      return ls;
    })();
    for(var file_i in file_list) {
      var file = file_list[file_i];
      var txt = fs.readfile(file);
      if(txt===undefined) throw new Error("Failed to read '" + file + "'");
      const txt_length = txt.length;
      txt = txt.replace(/[\r\t ]+[\n]/g,"\n");
      const n = txt_length - txt.length;
      if(n > 0) {
        print("File '" + file + "': Fixed " + n + " lines with trailing whitespaces." );
        fs.writefile(file, txt);
      }
    }
  };

  /**
   * Checks the versions specified in the gradle.properties against
   * the last readme.md changelog version. Applies to the CWD.
   * @returns {object}
   */
  me.tasks.version_check = function(allow_preversions) {
    var fails = [];
    const properties = me.parsing.gradle_properties("gradle.properties");
    const version_minecraft = properties[constants.gradle_property_version_minecraft()];
    const version_forge = properties[constants.gradle_property_version_forge()];
    const version_mod = properties[constants.gradle_property_modversion()];
    const combined_version = version_minecraft + "-" + version_mod;
    const readme_versions = me.parsing.readme_versions("readme.md").map(function(v){return v.replace(/^[v]/i, "").trim()});
    const readme_preversion_found = (readme_versions.filter(function(v){return v==("~v"+version_mod)}).length == 1);
    var readme_version_found = readme_versions.filter(function(v){return v==version_mod}).length == 1;
    if(allow_preversions && readme_preversion_found) readme_version_found = true;
    if(!readme_version_found) fails.push("Version 'v" + version_mod + "' not found in the readme changelog.");
    return {
      fails: fails,
      version_mod: version_mod,
      combined_version: combined_version,
      version_forge: version_forge,
      preversion_found: readme_preversion_found
    }
  };

  /**
   * Distribution JAR pre-checks.
   * @returns {array}
   */
  me.tasks.dist_check = function() {
    const uncommitted_changes = sys.shell("git status -s").trim();
    const gittags = sys.shell('git log -1 --format="%D"')
                    .replace(/[\s]/g,"").split(",")
                    .filter(function(s){ return s.indexOf("tag:")==0;})
                    .map(function(s){ return s.replace(/^tag:/,"");});
    const modversion = fs.readfile("gradle.properties", function(line){
      if(line.trim().indexOf(constants.gradle_property_modversion())!=0) return false;
      return line.replace(/^.*?=/,"").trim()
    }).trim();
    const mcversion = fs.readfile("gradle.properties", function(line){
      if(line.trim().indexOf("version_minecraft")!=0) return false;
      return line.replace(/^.*?=/,"").trim()
    }).trim();
    const git_remote = sys.shell("git remote -v").trim();
    const git_branch = sys.shell("git rev-parse --abbrev-ref HEAD").trim();
    const git_diff = sys.shell("git diff .").trim();
    var fails = [];
    if(modversion=="") fails.push("Could not determine '"+ constants.gradle_property_modversion() +"' from gradle properties.");
    if(!gittags.length) fails.push("Version not tagged.");
    const expected_commit_version = modversion.replace(/[-]/g,"") + "-mc" + mcversion;
    if(!gittags.filter(function(s){return s.indexOf(expected_commit_version)>=0}).length) fails.push("No tag version on this commit matching the gradle properties version (should be v" + expected_commit_version + ").");
    if(((!constants.options.without_ref_repository_check)) && (git_remote.replace(/[\s]/g,"").indexOf(constants.reference_repository() + "(push)") < 0)) fails.push("Not the reference repository.");
    if(git_branch != "develop") {
      fails.push("Not a valid branch for dist. (branch:'"+git_branch+"', must be 'develop')");
    }
    if(git_diff !== "") fails.push("Not everything committed to the GIT repository.");
    return fails;
  };

  /**
   * Returns a version check object for the given MC version.
   */
  me.tasks.changelog_data = function(mc_version) {
    if(mc_version===undefined) throw new Error("No MC version given for generating an update JSON.");
    mc_version = (""+mc_version).trim();
    function read_history() {
      var readme = fs.readfile(fs.cwd() + "/readme.md");
      if(!readme) throw new Error("Failed to load readme.md");
      readme = readme.split(/[\r]?[\n]/);
      while((readme.length > 0) && readme[0].search(/^## Version history/i)<0) readme.shift();
      // version history section
      if(!readme.length) throw new Error("Version history section not found in readme");
      readme.shift();
      var end_of_history = readme.length;
      for(var i=0; i<readme.length; ++i) if(readme[i].search(/^---/) >= 0) { end_of_history=i; break; }
      if(end_of_history >= readme.length) throw new Error("Could not find the end-of-history header marker.");
      // remove empty lines, splitters
      while(readme.length >= end_of_history) readme.pop();
      for(var i in readme) readme[i] = readme[i].replace(/[\s]+$/g,"").replace(/[\t]/g,"  ");
      readme = readme.filter(function(a){return a.replace(/[\s-]+/g,"")!="";});
      // condense multilines to single line entries for each fix or feature. ([A] ... [M] ...)
      for(var i=readme.length-1; i>0; --i) {
        var line = readme[i].replace(/^\s+/,"");
        if(line.search(/^[\[\-]/) < 0) {
          readme[i-1] += " " + line;
          readme[i] = "";
        }
      }
      readme = readme.filter(function(a){return a!="";});
      // Condense log entries sepatated with newlines to one line for each version
      for(var i=readme.length-1; i>0; --i) {
        var line = readme[i].replace(/^\s+/,"");
        if(line.search(/^[-~]/) < 0) {
          readme[i-1] += "\n" + line;
          readme[i] = "";
        }
      }
      readme = readme.filter(function(a){return a!="";});
      // Separate versions.
      var history = {};
      for(var i in readme) {
        var line = readme[i].replace(/^[\sv-]+/g,"").trim();
        var ver = line.substr(0, line.search(" ")).trim().toLowerCase();
        var txt = line.substr(line.search(" ")).trim();
        if(ver.search("~")===0) continue;
        if(history[ver] !== undefined) throw new Error("Double definition of version '" + ver + "' in the readme version history.");
        history[ver] = txt;
      }
      return history;
    }
    var history = read_history();
    var latest_release = "";
    var latest_beta = "";
    for(var ver in history) { latest_beta=ver; break; }
    for(var ver in history) if(ver.search(/(rc|b|a)/) < 0) { latest_release=ver; break; }
    var update_json = {}
    update_json["homepage"] = constants.project_download_inet_page();
    update_json[mc_version] = history;
    update_json["promos"] = {};
    update_json["promos"][""+mc_version+"-recommended"] = latest_release;
    update_json["promos"][""+mc_version+"-latest"] = latest_beta;
    return update_json;
  };

  // Standard tasks
  me.stdtasks = {};

  me.stdtasks["dist-check"] = function() {
    var fails = me.tasks.dist_check();
    if(fails.length == 0) return;
    for(var i in fails) fails[i] = "  - " + fails[i];
    alert("Dist check failed");
    alert(fails.join("\n")+"\n");
    exit(1);
  };
  me.stdtasks["version-check"] = function(args) {
    var r = me.tasks.version_check(!args.join().search("--no-preversions")>=0);
    if(r.fails.length == 0) return;
    alert("Version check failed:");
    for(var i in r.fails) alert("  - " + r.fails[i]);
    alert("Version data:");
    alert(" - version_mod       : '" + r.version_mod + "'");
    alert(" - combined_version  : '" + r.combined_version + "'");
    alert(" - version_forge     : '" + r.version_forge + "'");
    if(!!r.preversion_found) alert(" - PREVERSION FOUND  : '~" + r.version_mod + "'");
    exit(1);
  };
  me.stdtasks["version-html"] = function() {
    if(!fs.isdir("dist")) throw new Error("'dist' directory does not exist.");
    const hist = me.parsing.readme_history_section("readme.md");
    const version = me.parsing.version_data().combined;
    const modid = constants.modid;
    const html = "<pre>\n" + (hist.replace(/&/g, "&amp;").replace(/>/g, "&gt;").replace(/</g, "&lt;")) + "\n</pre>";
    fs.writefile("dist/" + modid + "-" + version + ".html", html);
  };
  me.stdtasks["tabs-to-spaces"] = function() {
    me.sanatizing.tabs_to_spaces(['java','lang']);
  };
  me.stdtasks["trailing-whitespaces"] = function() {
    me.sanatizing.remove_trailing_whitespaces(['java','json','lang']);
  };
  me.stdtasks["sanatize"] = function() {
    me.stdtasks["trailing-whitespaces"]();
    me.stdtasks["tabs-to-spaces"]();
  }
  me.stdtasks["dist"] = function() {
    me.stdtasks["version-html"]();
  };
  me.stdtasks["update-json"] = function() {
    const version_minecraft = me.parsing.gradle_properties("gradle.properties").version_minecraft;
    const json = me.tasks.changelog_data(version_minecraft);
    fs.mkdir("./meta");
    fs.writefile("./meta/update.json", JSON.stringify(json, null, 2));
  };
  me.stdtasks["dump-languages"] = function() {
    const lang_version = (me.parsing.gradle_properties("gradle.properties").version_minecraft.search("1.12.")==0) ? "1.12" : "1.13";
    const lang_extension = (lang_version == "1.12") ? ("lang") : ("json");
    const liblang = include("../meta/lib/liblang."+lang_version+".js")(constants);
    var lang_files = {};
    fs.find("./src/main/resources/assets/"+ constants.mod_registry_name() +"/lang", '*.'+lang_extension, function(f){
      const code = fs.basename(f).replace(/[\.].*$/,"").trim();
      const data = liblang.load(f);
      lang_files[code] = data;
      return false;
    });
    print(JSON.stringify(lang_files,null,1));
  };
  me.stdtasks["replace-model-paths"] = function(args) {
    const from = args[0];
    const to = args[1];
    const simulate = args[2];
    if(!from || !to) throw new Error("Usage: replace-model-paths <from> <to> <simulate=false>");
    const libassets = include("../meta/lib/libassets.js")(constants);
    const r = libassets.replace_model_paths(constants.local_assets_root(), from, to, !!simulate);
    var nerr = 0;
    var nok = 0;
    for(var p in r) if(!r[p]) ++nerr; else ++nok;
    if(Object.keys(r).length > 0) print('Replaced "'+from+'"->"'+to+'" in: ' + JSON.stringify(r,null,1));
    print(""+nok+" replaced" + ((nerr==0) ? "." : (", "+ nerr+" failed.")));
    return (nerr==0);
  }
  me.stdtasks["rename-files"] = function(args) {
    const dir = args[0];
    const ssearch = args[1];
    const sreplace = args[2];
    const simulate = args[3];
    if(!dir || !ssearch || !sreplace) throw new Error("Usage: rename-files <dir> <search> <replace> <simulate=false>");
    const libassets = include("../meta/lib/libassets.js")(constants);
    const r = libassets.rename_files(dir, ssearch, sreplace, simulate);
    var nerr = 0;
    var nok = 0;
    for(var p in r) if(!r[p]) ++nerr; else ++nok;
    if(Object.keys(r).length > 0) print('Renamed "'+ssearch+'"->"'+sreplace+'" for: ' + JSON.stringify(r,null,1));
    print(""+nok+" replaced" + ((nerr==0) ? "." : (", "+ nerr+" failed.")));
    return (nerr==0);
  }
  me.stdtasks["rename-block-model"] = function(args) {
    const libassets = include("../meta/lib/libassets.js")(constants);
    var dir = constants.local_assets_root()+"/models/block";
    const ssearch = args[0];
    const sreplace = args[1];
    const simulate = args[2];
    var fsearch = ssearch;
    var freplace = sreplace;
    var nerr=0, nok=0;
    if(!ssearch || !sreplace) throw new Error("Usage: rename-block-model <search> <replace> <simulate=false>");
    if((ssearch.search(/(\\|\/)/) >= 0) || (sreplace.search(/(\\|\/)/) >= 0)) {
      var d_ssearch = fs.dirname(ssearch);
      var d_sreplace = fs.dirname(sreplace);
      if(d_ssearch != d_sreplace) throw new Error("Only rename files, not move. The directories must be the same.");
      dir += "/" + d_ssearch;
      fsearch = fs.basename(ssearch);
      freplace = fs.basename(sreplace);
    }
    const file_replaces = libassets.rename_files(dir, fsearch, freplace, simulate);
    for(var p in file_replaces) if(!file_replaces[p]) ++nerr; else ++nok;
    if(Object.keys(file_replaces).length > 0) print('Renamed "'+fsearch+'"->"'+freplace+'" for: ' + JSON.stringify(file_replaces,null,1));

    const text_replaces = libassets.replace_model_paths(constants.local_assets_root(), ssearch, sreplace, simulate);
    for(var p in text_replaces) if(!text_replaces[p]) ++nerr; else ++nok;
    if(Object.keys(text_replaces).length > 0) print('Replaced "'+ssearch+'"->"'+sreplace+'" in: ' + JSON.stringify(text_replaces,null,1));

    print(""+nok+" replaced" + ((nerr==0) ? "." : (", "+ nerr+" failed.")));
    return (nerr==0);
  }

  /**
   * Task main
   */
  me.run = function(tasks, args, no_std_tasks, rel_root_path) {
    if(rel_root_path===undefined) rel_root_path = "..";
    if(!fs.chdir(fs.dirname(fs.realpath(sys.script)))) throw new Error("Failed to switch to mod source directory.");
    if(!fs.isdir(rel_root_path+"/.git")) throw new Error("Missing git repository in parent directory of mod source.");
    if(!no_std_tasks) {
      for(var key in me.stdtasks) {
        if(tasks[key]===undefined) tasks[key] = me.stdtasks[key];
      }
    }
    const task_name = args[0];
    var task_args = args.slice(1).map(function(v){return(v===undefined)?(""):(""+v).trim()}).filter(function(v){return(v.length>0)});
    if(task_name===undefined) {
      alert("No task specified.");
      exit(1);
    } else if((tasks[task_name])===undefined) {
      alert("No task '" + task_name + "' defined.");
      exit(1);
    } else {
      tasks[task_name](task_args);
    }
  };

  // Module include return value
  Object.freeze(me.parsing);
  Object.freeze(me.tasks);
  Object.freeze(me);
  return me;
});
