/**
 * easy xmlhttp framework 
 */

//const baseurl = window.location.protocol + "//" + window.location.host + "/IAFRalley";
let token=document.querySelector('meta[name="_csrf"]').getAttribute('content');
let header=document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    

function easyHTTP() {
  this.http = new XMLHttpRequest();
}

// Make an HTTP GET Request
easyHTTP.prototype.get = function(url, callback) {
  this.http.open('GET', url, true);
  this.http.setRequestHeader('Content-type', 'application/json');
  this.http.setRequestHeader(header, token);

  let self = this;
  this.http.onload = function() {
    if(self.http.readyState == 4 && self.http.status === 200) {
      callback(null, self.http.responseText);
    } else {
      callback('Error: ' + self.http.status);
    }
  }

  this.http.send();
}

// Make an HTTP POST Request
easyHTTP.prototype.post = function(url, data, callback) {
  this.http.open('POST',url, true);
  this.http.setRequestHeader('Content-type', 'application/json');
  this.http.setRequestHeader(header, token);
  let self = this;
  this.http.onload = function() {
    callback(null, self.http.responseText);
  }
  console.log(JSON.stringify(data));
  this.http.send(JSON.stringify(data));
}


// Make an HTTP PUT Request
easyHTTP.prototype.put = function(url, data, callback) {
  this.http.open('PUT', url, true);
  this.http.setRequestHeader('Content-type', 'application/json');
  this.http.setRequestHeader(header, token);
  let self = this;
  this.http.onload = function() {
    callback(null, self.http.responseText);
  }

  this.http.send(JSON.stringify(data));
}

// Make an HTTP DELETE Request
easyHTTP.prototype.delete = function(url, callback) {
  this.http.open('DELETE', url, true);
  this.http.setRequestHeader(header, token);
  let self = this;
  this.http.onload = function() {
    if(self.http.status === 200) {
      callback(null, 'Post Deleted');
    } else {
      callback('Error: ' + self.http.status);
    }
  }

  this.http.send();
}
