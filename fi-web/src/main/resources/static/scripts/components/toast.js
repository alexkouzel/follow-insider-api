export class Toast {
    constructor(text, props) {
        props = props ?? {};
        this.text = text;
        this.color = props['color'] ?? 'var(--red)';
        this.timeout = props['timeout'] ?? 3000;
    }

    build() {
        let toast = document.createElement('div');
        toast.style.background = this.color;
        toast.classList.add('toast', 'show');
        toast.innerText = this.text;
        return toast;
    }

    show() {
        if (document.querySelector('.toast') !== null) return;
        let toast = this.build();
        document.body.appendChild(toast);
        setTimeout(() => toast.remove(), this.timeout);
    }

    static showError(error) {
        new Toast(error, { color: '#c94e45' }).show();
    }

    static showSuccess(message) {
        new Toast(message, { color: 'var(--green)' }).show();
    }
}