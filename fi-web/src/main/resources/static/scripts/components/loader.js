export class Loader {
    constructor(parent) {
        this.parent = parent;
        this.ref = this.build();
        this.show();
    }

    build() {
        let loader = document.createElement('div');
        loader.classList.add('abs-ctr');
        loader.innerHTML = '<div class="lds-facebook"><div></div><div></div><div></div></div>';
        return loader;
    }

    show() {
        this.hide();
        if (this.parent === null) return;
        this.parent.appendChild(this.ref);
    }

    hide() {
        if (this.ref !== null) {
            this.ref.remove();
        }
    }
}