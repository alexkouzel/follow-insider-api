export class SearchBar {
    constructor(ref, loadHints, onSearch) {
        this.ref = ref;
        this.onSearch = onSearch;
        this.focus = -1;
        this.hintsNum = 0;
        this.handleHints(loadHints);
    }

    get hintsRef() {
        return this.ref.querySelectorAll('.hints>*');
    }

    handleHints(loadByInput) {
        let search = this;
        let inputRef = search.ref.querySelector('input');

        // update hints on search input
        let debounceTimeout;
        inputRef.addEventListener('input', function () {
            clearTimeout(debounceTimeout);
            debounceTimeout = setTimeout(async () => {
                let hints = await loadByInput(this.value);
                search.updateHints(hints, this.value);
            }, 300);
        });

        // handle hints' navigation & clicks
        inputRef.addEventListener('keydown', function (e) {
            switch (e.key) {
                case 'Down':
                case 'ArrowDown':
                    search.focus++;
                    search.updateActiveHint();
                    break;
                case 'Up':
                case 'ArrowUp':
                    search.focus--;
                    search.updateActiveHint();
                    break;
                case 'Enter':
                    let hints = search.hintsRef;
                    if (search.focus > -1 && hints) {
                        hints[search.focus].click();
                    } else {
                        search.onSearch(false, inputRef.value);
                    }
            }
        });

        // hide hints if clicked somewhere
        window.addEventListener('click', () => {
            search.resetHints();
        });
    }

    updateActiveHint() {
        let hintsNum = this.hintsNum;
        if (hintsNum === 0) return false;

        // shift current focus
        if (this.focus < 0) this.focus = (hintsNum - 1);
        this.focus = this.focus % hintsNum;

        // update an active hint
        let hints = this.hintsRef;
        for (let i = 0; i < hints.length; i++) {
            if (i === this.focus) {
                hints[i].classList.add('hints-active');
            } else {
                hints[i].classList.remove('hints-active');
            }
        }
    }

    updateHints(hints, input) {
        this.resetHints();

        // check if any matches found
        this.hintsNum = Object.keys(hints).length;
        if (this.hintsNum === 0) return;

        // create a list with hints
        let container = document.createElement('div');
        container.classList.add('hints');
        this.ref.appendChild(container);

        // add matched hints to the list
        input = input.toLowerCase();
        for (let val in hints) {
            let hint = document.createElement('p');
            let idx = val.toLowerCase().indexOf(input)
            if (idx !== -1) {
                let left = val.substring(0, idx);
                let middle = val.substring(idx, input.length + idx);
                let right = val.substring(input.length + idx, val.length);
                hint.innerHTML = `${left}<b>${middle}</b>${right}`;
            } else {
                hint.innerText = val;
            }
            hint.onclick = () => {
                let chosenHint = hints[val]
                this.onSearch(chosenHint !== null, chosenHint ?? val);
            };
            container.appendChild(hint);
        }
    }

    resetHints() {
        let hints = this.ref.querySelector('.hints');
        if (hints !== null) hints.remove();
        this.focus = -1;
    }
}