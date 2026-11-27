// Isolated test fixture. Install only in the regression run directory.
ServerEvents.recipes(event => {
    if (typeof KCookery === 'undefined' || typeof KCookerySoupBase === 'undefined') {
        throw new Error('Cookery scripting bindings are missing');
    }
    const r = event.recipes.kaleidoscope_cookery;
    r.pot('minecraft:bread', ['minecraft:wheat']).id('kubejs:cookery_regression/pot');
    r.flex_pot('minecraft:bread', ['minecraft:wheat']).id('kubejs:cookery_regression/flex_pot');
    r.stockpot('minecraft:bread', ['minecraft:wheat']).id('kubejs:cookery_regression/stockpot');
    r.flex_stockpot('minecraft:bread', ['minecraft:wheat']).id('kubejs:cookery_regression/flex_stockpot');
    r.millstone('minecraft:bread', 'minecraft:wheat').id('kubejs:cookery_regression/millstone');
    r.chopping_board(['minecraft:bread', 'minecraft:bone'], 'minecraft:wheat', 'minecraft:block/stone').id('kubejs:cookery_regression/chopping_board');
    r.steamer('minecraft:bread', 'minecraft:wheat').id('kubejs:cookery_regression/steamer');
    r.teapot('minecraft:bread', 'minecraft:wheat').id('kubejs:cookery_regression/teapot');
    r.bamboo_tray('minecraft:bread', 'minecraft:wheat', 'drying', 20).id('kubejs:cookery_regression/bamboo_tray');
});
