export class TimeRange {
    constructor() {
        this.id = uniqueId();
    }

    build() {
        let ranges = ['1M', '3M', '6M', '1Y', '3Y', '5Y', '10Y', 'MAX'];
        let hideOnMobile = ['3M', '3Y', '10Y'];
        let container = document.createElement('div');
        ranges.forEach(label => {
            let range = document.createElement('div');
            let id = this.id + '-' + label;
            if (hideOnMobile.includes(label))
                range.classList.add('hide-on-mobile');
            range.innerHTML = `
                <input type="checkbox" id="${id}" name="${label}">
                <label for="${id}">${label}</label>
            `;
            container.appendChild(range);
        });
        container.classList.add('time-ranges');
        container.id = this.id;
        return container;
    }

    init(initRange, onchange) {
        let timeRange = document.getElementById(this.id);

        // check the initial box
        let initBox = timeRange.querySelector(`input[name="${initRange}"]`);
        initBox.checked = true;

        // trigger checkbox onchange
        let boxes = timeRange.querySelectorAll('input[type=checkbox]');
        boxes.forEach(box => {
            box.onchange = (e) => {
                let target = e.currentTarget;
                if (target.checked) {
                    boxes.forEach(box => box.checked = false);
                    target.checked = true;
                    onchange(target.name);
                } else {
                    target.checked = true;
                }
            };
        });
    }

    get checked() {
        let checked = document.querySelector(`#${this.id} :checked`);
        return checked.getAttribute('name');
    }
}