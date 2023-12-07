export class Config {
    static debugMode = location.hostname === '127.0.0.1';
}

// Keep track of the next unique id
window.idCounter = 0;
window.uniqueId = function () {
    return 'id-' + idCounter++;
}

// Load scripts for header and footer
const head = document.getElementsByTagName('head')[0];
loadScript(head, '/scripts/components/footer.js');

function loadScript(head, url, isModule = false) {
    let script = document.createElement('script');
    script.type = isModule ? 'module' : 'text/javascript';
    script.src = url;
    head.appendChild(script);
}