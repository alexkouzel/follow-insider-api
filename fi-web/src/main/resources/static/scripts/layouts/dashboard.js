import { StringUtils } from '/scripts/utils/string.js';
import { Loader } from '/scripts/blocks/loader.js';

export class Dashboard {
    constructor(props) {
        props = props ?? {};
        this.initialTab = props.initialTab ?? 0;

        // show loading until build() is called
        const main = document.querySelector('body');
        this.loader = new Loader(main);

        // show rating on side panel (optional)
        this.ratings = [
            { value: 'Very good', box: 'green-box', color: 'var(--very-good)', from: 9, to: 10 },
            { value: 'Very bad', box: 'red-box', color: 'var(--very-bad)', from: 0, to: 1 },
            { value: 'So-so', box: 'yellow-box', color: 'var(--so-so)', from: 4, to: 6 },
            { value: 'Good', box: 'green-box', color: 'var(--good)', from: 7, to: 8 },
            { value: 'Bad', box: 'red-box', color: 'var(--bad)', from: 2, to: 3 }
        ];
    }

    build() {
        const dashboard = this;
        dashboard.loader.hide();
        document.querySelector('main').style.visibility = 'visible';
    }

    initTabs(tabs) {
        this.tabs = tabs;
        this.loadedTabs = [];

        // Create tab links and load the initial tab
        let links = document.getElementById('tabs');
        for (let i = 0; i < this.tabs.length; i++) {
            let link = document.createElement('a');
            link.innerText = this.tabs[i].name;
            link.onclick = () => this.loadTab(i);
            links.appendChild(link);
        }
        this.loadTab(this.initialTab);
    }

    loadTab(index) {
        if (this.tabs.length <= index) {
            throw 'invalid tab index: ' + index;
        }
        // remove any popups
        let toast = document.querySelector('.toast');
        if (toast) toast.remove();

        // highlight chosen tab
        let links = document.querySelectorAll('#tabs a');
        links.forEach(link => link.classList.remove('chosen'));
        links[index].classList.add('chosen');

        // load tab content
        let content = document.getElementById('tab-content');
        content.innerHTML = '';
        content.appendChild(this.tabs[index].content);

        // call tab ready function
        let firstLoad = !this.loadedTabs.includes(index);
        this.tabs[index].ready(firstLoad);
        this.loadedTabs.push(index);
    }

    onResize(func) {
        let resizeEvent;
        window.addEventListener('resize', () => {
            clearTimeout(resizeEvent);
            resizeEvent = setTimeout(func, 100);
        });
    }

    fillPanel(data) {
        data.lastActive = data.lastActive ? StringUtils.formatDate(data.lastActive) : '-';
        data.url = data.url ? 'https://logo.clearbit.com/' + data.url : data.defaultUrl;

        document.getElementById('panel').innerHTML = `
            <div id="panel-info">
                <div id="panel-intro">
                    <div id="panel-image">
                        <img src='${data.url}' onerror="this.src='${data.defaultUrl}'" alt="panel image">
                    </div>
                    <div style="margin-left: 3rem; gap: 1rem" class="col">
                        <h3>${data.name}</h3>
                        <p>Last active: ${data.lastActive}</p>
                    </div>
                </div>
                <!-- <div class="col rating"></div> -->
            </div>`;
    }

    addScore(container, text, value) {
        let score = document.createElement('div');
        score.innerHTML = `
            <h3>${text}</h3>
            <div class="score"></div>`;

        if (text === 'Overall')
            score.classList.add('overall');

        let boxPath = (name) => `/assets/images/boxes/${name}.jpg`;
        let rating = this.ratings.find(r => (value >= r.from) && (value <= r.to));
        let boxes = score.querySelector('.score');
        for (let i = 0; i < 10; i++) {
            let box = document.createElement('img');
            box.alt = 'box image ' + i;
            box.src = !rating || (value <= i) 
                ? boxPath('grey-box') 
                : boxPath(rating.box);
            boxes.appendChild(box);
        }
        container.appendChild(score);
    }
}

export class Tab {
    constructor(name, content, ready) {
        this.name = name;
        this.content = content;
        this.ready = ready;
    }
}