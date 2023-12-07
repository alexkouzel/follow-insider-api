export class HttpUtils {

    static get getLastUrlSegment() {
        let url = location.href.split('?')[0];
        let segments = url.split('/');
        return segments.pop() || segments.pop();
    }

    static deleteRaw(url) {
        console.log('DELETE :: ' + url);
        return this._request(url, {method: 'DELETE'});
    }

    static post(url) {
        console.log('POST :: ' + url);
        return this._request(url, {method: 'POST'});
    }

    static postJson(url, json) {
        console.log('POST JSON :: ' + url);
        return this._request(url, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(json)
        });
    }

    static postForm(url, form) {
        console.log('POST FORM :: ' + url);
        return this._request(url, {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'},
            body: new URLSearchParams(form),
        });
    }

    static getText(url) {
        return this._request(url, {
            method: 'GET',
            headers: {'Accept': 'text/plain'},
        }).then(data => {
            if (!data) throw new Error('Invalid data');
            return data.text();
        });
    }

    static getJson(url) {
        return this._request(url, {
            method: 'GET',
            headers: {'Accept': 'application/json'},
        }).then(data => {
            if (!data) throw new Error('Invalid data');
            return data.json();
        });
    }

    static _request(url, props) {
        props['redirect'] = 'follow';
        return fetch(location.origin + url, props)
            .then(response => this._handleResponse(response));
    }

    static _handleResponse(response) {
        if (response.redirected) {
            window.location.href = response.url;
        }
        if (response.ok) {
            return response;
        }
        return response.json().then(errorData => {
            throw new Error(errorData.message || response.statusText);
        });
    }

}