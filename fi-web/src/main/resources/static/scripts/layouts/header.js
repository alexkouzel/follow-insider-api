import { SearchBar } from '/scripts/blocks/search.js';
import { Account } from '/scripts/account/account.js';
import { Http } from '/scripts/utils/http.js';
import { Config } from '/scripts/root.js';

customElements.define('main-header', class extends HTMLElement {
    constructor() {
        super();
        this.innerHTML = `
            <div>
                <h2 onclick="location.assign('/')">FI</h2>
                <div id="search" class="search header-search">
                    <label for="search-input"></label>
                    <input id="search-input" type="text" placeholder="Search a public company">
                    <img class="abs-ctr" src="/assets/icons/search.svg" alt="Search Icon">
                </div>

                <!-- Login button -->
                <button class="btn-main btn-white login-btn">Login</button>
                
                <!-- Dropdown profile menu -->
                <div class="col dd-menu">
                    <div class="col mini-profile">
                        <div class="avatar"></div>
                        <div class="col" style="gap: 5px">
                            <p id="dd-name"></p>
                            <p id="dd-email" style="opacity: 0.4"></p>
                        </div>
                        <div class="divider"></div>
                    </div>
                    <div class="col">
                        <!-- <div class="dd-link" onclick="location.assign('/dev/profile')"><p>Profile</p></div> -->
                        <!-- <div class="dd-link" onclick="location.assign('/dev/settings')"><p>Settings</p></div> -->
                        <div class="dd-link" id="logout-link"><p>Log out</p></div>
                    </div>
                </div>

                <button class="nav-btn">
                    <input id="nav-box" class="nav-box" type="checkbox"/>
                    <label for="nav-box"><span class="nav-icon"></span></label>
                </button>
            </div>
            <ul class="header-menu nav-links">
            <li onclick="location.assign('/insider-trading')"><p>Insider Trading</p></li>
            <li onclick="location.assign('/contact')"><p>Contact</p></li>
            <li onclick="location.assign('/about')"><p>About</p></li>
            <li onclick="location.assign('/privacy')"><p>Privacy</p></li>
            </ul>
        `;
        this.initBurger();
        this.initSearch()
        this.initAccount();
    }

    initAccount() {
        let loginBtn = this.querySelector('.login-btn');
        loginBtn.onclick = () => location.assign('/login');
        this.querySelector('#logout-link').onclick = () => Account.logout();

        // fetch user profile if exists
        Account.loadProfile().then((profile) => {

            // configure dropdown menu
            let dd = document.querySelector('.dd-menu');
            dd.querySelector('.avatar').onclick = () => location.assign('/profile');
            window.addEventListener('click', () => hideDropdown(dd))

            // configure user menu
            let menu = document.createElement('div');
            menu.innerHTML = `<div class="avatar"></div><p></p>`;
            menu.classList.add('user-menu');
            menu.onclick = (e) => {
                e.stopPropagation(); // don't hide dropdown menu
                let hidden = window.getComputedStyle(dd).opacity === '0';
                hidden ? showDropdown(dd) : hideDropdown(dd);
            };
            document.addEventListener('update-profile', () => {
                Account.loadProfile().then((profile) => setProfile(profile));
            });

            // replace login button
            setProfile(profile);
            loginBtn.after(menu);
            loginBtn.remove();

            function showDropdown(dd) {
                dd.style.opacity = '1';
                dd.style.visibility = 'visible';
            }

            function hideDropdown(dd) {
                dd.style.opacity = '0';
                setTimeout(() => {
                    if (dd.style.opacity === '0') {
                        dd.style.visibility = 'hidden'
                    }
                }, 500)
            }

            function setProfile(profile) {
                let avatarUrl = profile.avatarUrl ?? '/assets/images/profile.webp'
                dd.querySelector('#dd-email').innerText = profile.email;
                dd.querySelector('#dd-name').innerText = profile.name;
                dd.querySelector('.avatar').style.backgroundImage = `url(${avatarUrl})`;
                menu.querySelector('.avatar').style.backgroundImage = `url(${avatarUrl})`;
                menu.querySelector('p').innerText = profile.name;
            }
        }).catch(() => console.log('profile is not loaded'));
    }

    initBurger() {
        this.querySelector('.nav-btn').onclick = function () {
            let box = document.querySelector('.nav-box');
            document.querySelector('.header-menu').style.maxHeight = box.checked ? '0' : '250px';
            box.checked = !box.checked;
        };
    }

    initSearch() {
        let searchRefs = document.querySelectorAll('.search')
        searchRefs.forEach(searchRef => {
            if (Config.debugMode) {
                let testHints = {
                    "APPLE INC (AAPL)": "0000320191",
                    "Tesla, Inc. (TSLA)": "9990320192",
                    "Mastercard Inc. (MA)": "0205320193",
                    "Coca Cola Co. (KO)": "4060320194",
                };
                new SearchBar(searchRef,
                    (input) => input === '' ? [] : testHints,
                    (byHint, query) => console.log(`query: ${query}, byHint: ${byHint}`),
                );
            } else {
                new SearchBar(searchRef,
                    (input) => Http.getJson('/search/hints?limit=6&q=' + input),
                    (byHint, query) => {
                        let path = byHint ? '/stocks/' : '/search?q=';
                        location.assign(path + query);
                    });
            }
        });
    }
});