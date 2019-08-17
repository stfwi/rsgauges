"use strict";
(function(constants){
  var me = {};


  /**
   * Determines a list of all textures in the given path as plain object,
   * where the keys are the unified texture path (e.g. "block/", not "blocks/"),
   * and the value an object containing file path, SHA1, size, etc.
   * @returns {object}
   */
  me.load_texture_data = function(textures_path) {
    const wd = fs.cwd();
    var data = {};
    try {
      if(!fs.chdir(textures_path)) throw new Error("Texture root path does not exist: '" + textures_path + "'");
      fs.find(".", '*.*', function(path) {
        const file = path.replace(/[\\]/g, "/").replace(/^\.\//,"");
        const unified_file = file.replace(/^blocks\//, "block/");
        data[unified_file] = { path:file, size:fs.size(file), sha:sys.hash.sha1(path, true) };
        return false;
      });
      return data;
    } finally {
      fs.chdir(wd);
    }
  }

  /**
   * Compares texture files and mcdata files two given assets paths, returns the
   * lists of both file trees and the differences as object.
   * @returns {object}
   */
  me.compare_textures = function(assets_path_a, assets_path_b) {
    const txpath_a = assets_path_a + "/" + constants.mod_registry_name() + "/textures";
    const txpath_b = assets_path_b + "/" + constants.mod_registry_name() + "/textures";
    const a = me.load_texture_data(txpath_a);
    const b = me.load_texture_data(txpath_b);
    const txpath_a_is112 = fs.isdir(txpath_a + "/blocks");
    const txpath_b_is112 = fs.isdir(txpath_b + "/blocks");
    const cmp = {a:{},b:{}};
    cmp.a.path = txpath_a;
    cmp.b.path = txpath_b;
    cmp.a.is112 = txpath_a_is112;
    cmp.b.is112 = txpath_b_is112;
    cmp.a.files = Object.assign({},a);
    cmp.b.files = Object.assign({},b);
    cmp.match = {}
    cmp.differ = {}
    cmp.onlyin_a = {}
    cmp.onlyin_b = {}
    for(var key in a) {
      if(b[key] === undefined) {
        cmp.onlyin_a[key] = a[key];
        continue;
      }
      if(a[key].sha === b[key].sha) {
        cmp.match[key] = a[key];
        b[key]=undefined; delete b[key];
      } else {
        cmp.differ[key] = { a:a[key], b:b[key] };
        b[key]=undefined; delete b[key];
      }
    }
    a = undefined;
    for(var key in b) {
      cmp.onlyin_b[key] = b[key];
    }
    b = undefined;
    return cmp;
  };

  /**
   * Loads all blockstate files in the given assets path, and returns the parsed JSON
   * data as plain object, where the keys are the blockstate names, and the value the
   * parsed JSON files.
   * @returns {object}
   */
  me.load_blockstates = function(assets_path) {
    const wd = fs.cwd();
    var data = {};
    try {
      if(!fs.chdir(assets_path+"/blockstates")) throw new Error("blockstates path not found in: '" + assets_path + "'");
      fs.find(".", '*.json', function(path) {
        const file = path.replace(/[\\]/g, "/").replace(/^\.\//,"");
        if(fs.basename(file) != fs.basename(file).toLowerCase()) throw new Error("Blockstate file must be lowercase: '"+file+"'"); // hard fail
        const blockstate_name = fs.basename(file).replace(/[\.]json/i, "");
        if(blockstate_name.search(/[^a-z0-9_]/) >= 0) throw new Error("Blockstate file name contains invalid characters: '"+file+"'"); // here, too
        var json = fs.readfile(path);
        if(json===undefined) throw new Error("Failed to read blockstate file '"+file+"' (could not open file)");
        try { json=JSON.parse(json); } catch(ex) { throw new Error("Failed to parse blockstate file '"+file+"' (invalid JSON)"); }
        data[blockstate_name] = {file:(assets_path+"/blockstates/"+file), data:json};
        return false;
      });
      return data;
    } finally {
      fs.chdir(wd);
    }
  };

  me.compare_blockstates = function(assets_path_a, assets_path_b) {
    const a = me.load_blockstates(assets_path_a);
    const b = me.load_blockstates(assets_path_b);
    const onlyin_a = {};
    const onlyin_b = {};
    for(var key in a) {
      if(b[key] === undefined) {
        onlyin_a[key] = a[key];
        continue;
      } else {
        b[key]=undefined; delete b[key];
      }
    }
    a = undefined;
    for(var key in b) {
      onlyin_b[key] = b[key];
    }
    b = undefined;
    return {
      onlyin_a: onlyin_a,
      onlyin_b: onlyin_b,
    }
  };

  /**
   * Replaces model paths in blockstate and model files. Uses text
   * substitution, not JSON load->modify->save.
   * The `form` and `to` must be full
   */
  me.replace_model_paths = function(assets_path, from, to, nowrite) {
    var affected_files = {};
    const wd = fs.cwd();
    const reesc = function(re) {return re.replace(/[.*+?^${}()|[\]\\]/g,'\\$&');};
    try {
      if(!fs.chdir(assets_path)) throw new Error("Failed to switch to assets directory '" + assets_path + "'");
      fs.find("./blockstates", '*.json', function(path) {
        const txt = fs.readfile(path);
        if(txt===undefined) return;
        var sre = '("model"[\\s]*:[\\s]*"' + constants.modid + ':';
        sre += (txt.search(new RegExp(sre+'block/)')) >= 0) ? 'block/)' : ')'; // 1.13/14
        const rtxt = txt.replace(new RegExp(sre+reesc(from), "g"), '$1'+to);
        const tpath = path.replace(/[\\]/g,"/").replace(/^[\.][\/]/,"");
        if(txt!=rtxt) affected_files[tpath] = (!!nowrite) || (!!fs.writefile(path, rtxt));
      });
      fs.find("./models", '*.json', function(path) {
        const txt = fs.readfile(path);
        if(txt===undefined) return;
        var sre = '("(parent|submodel)"[\\s]*:[\\s]*"' + constants.modid + ':block/)';
        const rtxt = txt.replace(new RegExp(sre+reesc(from), "g"), '$1'+to);
        const tpath = path.replace(/[\\]/g,"/").replace(/^[\.][\/]/,"");
        if(txt!=rtxt) affected_files[tpath] = (!!nowrite) || (!!fs.writefile(path, rtxt));
      });
      return affected_files;
    } finally {
      fs.chdir(wd);
    }
  };

  /**
   * File name text replacing.
   */
  me.rename_files = function(dir, ssearch, sreplace, nowrite) {
    if(!fs.isdir(dir)) throw new Error("replace-in-filenames: Directory not existing: '"+dir+"'");
    const affected_files = {};
    const wd = fs.cwd();
    try {
      if(!fs.chdir(dir)) throw new Error("Failed to switch to directory '" + dir + "'");
      const files = fs.readdir(".");
      for(var i in files) {
        const f0 = files[i];
        const f1 = f0.replace(ssearch, sreplace);
        if(f0 != f1) {
          affected_files[f0] = f1;
          if(!nowrite) {
            fs.rename(f0, f1);
          }
        }
      }
      return affected_files;
    } finally {
      fs.chdir(wd);
    }
  }

  Object.freeze(me);
  return me;
});